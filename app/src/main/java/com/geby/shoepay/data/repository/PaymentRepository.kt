package com.geby.shoepay.data.repository

import ApiConfig
import com.geby.shoepay.response.CreateTransactionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PaymentRepository {

    suspend fun getPaymentChannels() = withContext(Dispatchers.IO) {
        ApiConfig.tripayApi.getPaymentChannels()
    }

    suspend fun createTopUp(topUpRequest: CreateTransactionRequest) = withContext(Dispatchers.IO) {
        ApiConfig.tripayApi.createTopUp(topUpRequest)
    }

    suspend fun getDetailPayment(reference: String) = withContext(Dispatchers.IO) {
        ApiConfig.tripayApi.getDetailPayment(reference)
    }
}