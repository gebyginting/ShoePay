package com.geby.shoepay.ui.payment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.geby.shoepay.R
import com.geby.shoepay.data.adapter.ChannelAdapter
import com.geby.shoepay.data.models.PaymentHistory
import com.geby.shoepay.data.models.UserInfo
import com.geby.shoepay.databinding.ActivityPaymentBinding
import com.geby.shoepay.response.OrderItem
import com.geby.shoepay.utilities.Helper
import com.geby.shoepay.utilities.ResultState
import com.geby.shoepay.utilities.UserPreference
import com.geby.shoepay.viewmodel.PaymentViewModel
import com.geby.shoepay.viewmodel.ViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val paymentViewModel: PaymentViewModel by viewModels { factory }
    private lateinit var userPreference: UserPreference
    private lateinit var firestore: FirebaseFirestore
    private var selectedMethod: String? = null

    companion object {
        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_PRICE = "EXTRA_PRICE"
        const val EXTRA_QUANTITY = "EXTRA_QUANTITY"
        const val DEFAULT_PHONE = "08123456789"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        firestore = FirebaseFirestore.getInstance()
        userPreference = UserPreference(this)

        observeChannels()         // 👈 hanya sekali
        paymentViewModel.loadChannels()  // 👈 load pertama kali
        observeEstimatedPayment()
//        setupPayButton()
        dummyPayButton()
//        logout()
        setupRetryButton()

    }

    private fun observeChannels() {
        binding.rvChannels.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                paymentViewModel.channelsState.collect { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            binding.includeLoadingLayout.progressBar.visibility = View.VISIBLE
                            binding.includeLoadingLayout.btnRetry.visibility = View.GONE
                        }

                        is ResultState.Success -> {
                            binding.includeLoadingLayout.progressBar.visibility = View.GONE
                            binding.rvChannels.adapter = ChannelAdapter(result.data) { selected ->
                                selectedMethod = selected.code
                                paymentViewModel.selectedChannel.value = selected
                                binding.btnPay.isEnabled = true
                                binding.btnPay.setBackgroundColor(getColor(R.color.black))

                                val orderItems = getOrderItems()
                                selected.feeCustomer?.percent?.let {
                                    paymentViewModel.calculateEstimatedPayment(
                                        customerFee = it.toInt(),
                                        items = orderItems
                                    )
                                }
                            }
                        }

                        is ResultState.Error -> {
                            binding.includeLoadingLayout.progressBar.visibility = View.VISIBLE
                            binding.includeLoadingLayout.progressBar.visibility = View.GONE
                            binding.includeLoadingLayout.tvLoading.text = "Terjadi kesalahan. Silakan coba lagi"
                            binding.includeLoadingLayout.btnRetry.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun setupRetryButton() {
        binding.includeLoadingLayout.btnRetry.setOnClickListener {
            showLoadingStateImmediately()
            paymentViewModel.loadChannels()
        }
    }

    private fun observeEstimatedPayment() {
        paymentViewModel.estimatedPayment.observe(this) { (amount, fee, total) ->
            binding.subTotal.text = Helper.rupiahFormat(amount)
            binding.adminFee.text = Helper.rupiahFormat(fee)
            binding.total.text = Helper.rupiahFormat(total)
        }
    }

    private fun showLoadingStateImmediately() {
        with(binding.includeLoadingLayout) {
            progressBar.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            tvLoading.text = "Memuat data."
            btnRetry.visibility = View.GONE
        }
    }

    private fun setupPayButton() {
        binding.btnPay.setOnClickListener {
            val selected = paymentViewModel.selectedChannel.value
            if (selected == null) {
                Toast.makeText(this, "Silakan pilih metode pembayaran", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val orderItems = getOrderItems()
            val user = getUserInfo()

            // Siapkan request
            val request = paymentViewModel.preparePaymentData(
                selectedChannelCode = selected.code ?: "",
                customerName = user.name,
                customerEmail = user.email,
                customerPhone = DEFAULT_PHONE,
                orderItems = orderItems
            )

            // Kirim permintaan transaksi
            paymentViewModel.sendPaymentRequest(request)

            // Observasi hasilnya
            paymentViewModel.topUpResult.observe(this) { response ->
                if (response.success && response.data != null) {

                    savePaymentHistory(
                        payment = PaymentHistory(
                            userId = user.uid,
                            username = user.name,
                            email = user.email,
                            payment_method = response.data.paymentMethod,
                            amount = response.data.amount,
                            status = response.data.status,
                            orderItem = orderItems.first().name,
                            orderQuantity = EXTRA_QUANTITY.toInt()
                        )
                    )

                    // Tampilkan dialog dengan data transaksi
                    val dialog = PaymentDetailDialogFragment.newInstance(
                        reference = response.data.reference,
                    )
                    dialog.show(supportFragmentManager, "PaymentDetailDialog")
                } else {
                    Toast.makeText(this, "Gagal membuat transaksi: ${response.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun dummyPayButton() {
        binding.btnPay.setOnClickListener {
            // Tampilkan dialog dengan data transaksi
            val dialog = PaymentDetailDialogFragment.newInstance(
                reference = "DEV-T40011231864GBBSW",
            )
            dialog.show(supportFragmentManager, "PaymentDetailDialog")        }
    }

    private fun getOrderItems(): List<OrderItem> {
        val name = intent.getStringExtra(EXTRA_NAME).orEmpty()
        val price = intent.getIntExtra(EXTRA_PRICE, 0)
        val quantity = intent.getIntExtra(EXTRA_QUANTITY, 0)
        return listOf(OrderItem(name, price,quantity ))
    }

    private fun savePaymentHistory(payment: PaymentHistory) {
        firestore.collection("payment_histories")
            .add(payment)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Payment history saved with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to save payment history", e)
            }
    }

    private fun getUserInfo(): UserInfo {
        return UserInfo(
            uid = userPreference.getUid(),
            name = userPreference.getName(),
            email = userPreference.getEmail()
        )
    }

}


