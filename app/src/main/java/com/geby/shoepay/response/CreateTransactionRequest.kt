package com.geby.shoepay.response

import com.google.gson.annotations.SerializedName

data class CreateTransactionRequest(
    @SerializedName("method") val method: String, // Contoh: "BRIVA", "OVO"
    @SerializedName("merchant_ref") val merchantRef: String,
    @SerializedName("amount") val amount: Int,
    @SerializedName("customer_name") val customerName: String,
    @SerializedName("customer_email") val customerEmail: String,
    @SerializedName("customer_phone") val customerPhone: String,
    @SerializedName("order_items") val orderItems: List<OrderItem>,
//    @SerializedName("expired_time") val expiredTime: Int,
    @SerializedName("signature") val signature: String
)

data class OrderItem(
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("quantity") val quantity: Int,
)