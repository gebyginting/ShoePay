package com.geby.shoepay.data.retrofit

import com.geby.shoepay.response.CreateTransactionRequest
import com.geby.shoepay.response.CreateTransactionResponse
import com.geby.shoepay.response.DetailPaymentResponse
import com.geby.shoepay.response.TripayResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("merchant/payment-channel")
    suspend fun getPaymentChannels(): Response<TripayResponse>

    @POST("transaction/create")
    suspend fun createTopUp(
        @Body requestBody: CreateTransactionRequest
    ): Response<CreateTransactionResponse>

    @GET("transaction/detail")
    suspend fun getDetailPayment(
        @Query("reference") reference: String
    ): Response<DetailPaymentResponse>
//    @POST("transaction/create")
//    suspend fun createTopUp(
//        @Body request: TopUpRequest   // Data request
//    ): Response<RequestResponse>
}
