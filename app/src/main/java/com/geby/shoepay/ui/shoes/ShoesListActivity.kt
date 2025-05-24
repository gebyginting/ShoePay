package com.geby.shoepay.ui.shoes

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.geby.shoepay.R
import com.geby.shoepay.data.adapter.ShoeAdapter
import com.geby.shoepay.data.models.Shoe
import com.geby.shoepay.databinding.ActivityBookListBinding
import com.geby.shoepay.ui.auth.SignInActivity
import com.geby.shoepay.ui.detail.DetailShoeDialogFragment
import com.geby.shoepay.ui.payment.PaymentHistoryActivity
import com.geby.shoepay.utilities.Constants
import com.google.firebase.auth.FirebaseAuth

class ShoesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookListBinding
    private lateinit var shoeAdapter: ShoeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        checkUser()
        setupShoesList()
        showHistory()
    }

    private fun setupShoesList() {
        shoeAdapter = ShoeAdapter(Constants.dummyShoeList) { shoe ->
            gotoPayment(shoe)
        }

        binding.rvShoes.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = shoeAdapter
        }

        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            val filteredList = when (checkedId) {
                R.id.chip_sneakers -> Constants.dummyShoeList.filter { it.shoeType.equals("Sneakers", true) }
                R.id.chip_heels -> Constants.dummyShoeList.filter { it.shoeType.equals("Heels", true) }
                R.id.chip_boots -> Constants.dummyShoeList.filter { it.shoeType.equals("Boots", true) }
                else -> Constants.dummyShoeList // All selected
            }
            shoeAdapter.updateList(filteredList)
        }
    }


    private fun gotoPayment(shoe: Shoe) {
        val dialog = DetailShoeDialogFragment.newInstance(shoe)
        dialog.show(supportFragmentManager, "DetailShoeDialog")
    }


    private fun checkUser() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun showHistory() {
        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, PaymentHistoryActivity::class.java))
        }
    }
}