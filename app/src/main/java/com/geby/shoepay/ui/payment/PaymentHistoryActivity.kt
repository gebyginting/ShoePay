package com.geby.shoepay.ui.payment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.geby.shoepay.data.adapter.PaymentHistoryAdapter
import com.geby.shoepay.databinding.ActivityPaymentHistoryBinding
import com.geby.shoepay.utilities.ResultState
import com.geby.shoepay.viewmodel.UserViewModel
import com.geby.shoepay.viewmodel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class PaymentHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentHistoryBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val userViewModel: UserViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        setupPaymentHistories()
    }

    private fun setupPaymentHistories() {
        binding.includeLoadingLayout.progressBar.visibility = View.VISIBLE
        val rv = binding.recyclerHistory
        val uid = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

        userViewModel.fetchUserHistories(uid)
        lifecycleScope.launch {
            userViewModel.userHistoriesState.collect { state ->
                when (state) {
                    is ResultState.Loading -> binding.includeLoadingLayout.progressBar.visibility = View.VISIBLE
                    is ResultState.Success -> {
                        binding.includeLoadingLayout.progressBar.visibility = View.GONE
                        if (state.data.isNotEmpty()) {
                            rv.layoutManager = LinearLayoutManager(this@PaymentHistoryActivity)
                            rv.adapter = PaymentHistoryAdapter(state.data)
                            binding.emptyListMessage.visibility = View.GONE
                        } else {
                            binding.emptyListMessage.visibility = View.VISIBLE
                        }
                    }
                    is ResultState.Error -> Toast.makeText(this@PaymentHistoryActivity, state.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
