package com.geby.shoepay

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geby.shoepay.databinding.ActivityMainBinding
import com.geby.shoepay.response.DataItem
import com.geby.shoepay.ui.payment.PaymentActivity
import com.geby.shoepay.viewmodel.PaymentViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PaymentViewModel by viewModels()
    private lateinit var rvChannels: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvChannels = findViewById(R.id.rvChannels)
        rvChannels.layoutManager = LinearLayoutManager(this)

        viewModel.loadChannels()


    }
    private fun openPaymentActivity(channel: DataItem) {
        val intent = Intent(this, PaymentActivity::class.java)
        viewModel.selectedChannel.value = channel
        startActivity(intent)
    }
}