package com.geby.shoepay.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geby.shoepay.BuildConfig.MERCHANT_CODE
import com.geby.shoepay.BuildConfig.PRIVATE_KEY
import com.geby.shoepay.data.repository.PaymentRepository
import com.geby.shoepay.generateSignature
import com.geby.shoepay.response.CreateTransactionRequest
import com.geby.shoepay.response.CreateTransactionResponse
import com.geby.shoepay.response.DataItem
import com.geby.shoepay.response.DetailPaymentResponse
import com.geby.shoepay.response.OrderItem
import com.geby.shoepay.utilities.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentViewModel(private val repository: PaymentRepository) : ViewModel() {

    private val _channelsState = MutableStateFlow<ResultState<List<DataItem>>>(ResultState.Loading)
    val channelsState: MutableStateFlow<ResultState<List<DataItem>>> = _channelsState

    private val _topUpResult = MutableLiveData<CreateTransactionResponse>()
    val topUpResult: LiveData<CreateTransactionResponse> get() = _topUpResult

    val selectedChannel = MutableLiveData<DataItem>()

    private val _estimatedPayment = MutableLiveData<Triple<Int, Int, Int>>()
    val estimatedPayment: LiveData<Triple<Int, Int, Int>> get() = _estimatedPayment

    private val _detailPayment = MutableLiveData<DetailPaymentResponse>()
    val detailPayment: LiveData<DetailPaymentResponse> get() = _detailPayment

    fun loadChannels() {
        viewModelScope.launch {
            _channelsState.value = ResultState.Loading
            try {
                val response = repository.getPaymentChannels()
                if (response.isSuccessful) {
                    val data = response.body()?.data?.filterNotNull() ?: emptyList()
                    _channelsState.value = ResultState.Success(data)
                } else {
                    _channelsState.value = ResultState.Error("Gagal mengambil data channel pembayaran")
                }
            } catch (e: Exception) {
                _channelsState.value = ResultState.Error(e.message.toString())
            }
        }
    }

    fun preparePaymentData(
        selectedChannelCode: String,
        customerName: String,
        customerEmail: String,
        customerPhone: String,
        orderItems: List<OrderItem>
    ): CreateTransactionRequest {
        val privateKey = PRIVATE_KEY
        val merchantCode = MERCHANT_CODE
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val merchantRef = "PX" + dateFormat.format(Date())
        val amount = orderItems.sumOf { it.price * it.quantity }
        val signature = generateSignature(merchantCode, merchantRef, amount, privateKey)

        return CreateTransactionRequest(
            method = selectedChannelCode,
            merchantRef = merchantRef,
            amount = amount,
            customerName = customerName,
            customerEmail = customerEmail,
            customerPhone = customerPhone,
            orderItems = orderItems,
            signature = signature
        )
    }

    fun calculateEstimatedPayment(customerFee: Int, items: List<OrderItem>) {
        val amount = items.sumOf { it.price * it.quantity }
        val fee = ((amount * customerFee) / 100)
        val total = amount + fee

        _estimatedPayment.postValue(Triple(amount, fee, total))
    }

    fun sendPaymentRequest(topUpRequest: CreateTransactionRequest) {
        viewModelScope.launch {
            try {
                val response = repository.createTopUp(topUpRequest)
                if (response.isSuccessful) {
                    val body = response.body()
                    _topUpResult.postValue(body ?: CreateTransactionResponse(false, "Response null", null))
                } else {
                    val errorMsg = response.errorBody()?.string()
                    _topUpResult.postValue(CreateTransactionResponse(false, "Gagal: $errorMsg", null))
                }
            } catch (e: Exception) {
                _topUpResult.postValue(CreateTransactionResponse(false, "Exception: ${e.message}", null))
            }
        }
    }

    fun getDetailPayment(reference: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDetailPayment(reference)
                if (response.isSuccessful) {
                    val body = response.body()
                    _detailPayment.postValue(body!!)
                } else {
                    Log.e("PaymentViewModel", "Gagal: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "Exception: ${e.message}")
            }
        }
    }
}