package com.geby.shoepay.response

import com.google.gson.annotations.SerializedName

data class CreateTransactionResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: TransactionData?
)

data class TransactionData(
    @SerializedName("reference") val reference: String,
    @SerializedName("merchant_ref") val merchantRef: String,
    @SerializedName("payment_selection_type") val paymentSelectionType: String,
    @SerializedName("payment_method") val paymentMethod: String,
    @SerializedName("payment_name") val paymentName: String,
    @SerializedName("customer_name") val customerName: String,
    @SerializedName("customer_email") val customerEmail: String,
    @SerializedName("customer_phone") val customerPhone: String,
    @SerializedName("callback_url") val callbackUrl: String,
    @SerializedName("return_url") val returnUrl: String,
    @SerializedName("amount") val amount: Int,
    @SerializedName("fee_merchant") val feeMerchant: Int,
    @SerializedName("fee_customer") val feeCustomer: Int,
    @SerializedName("total_fee") val totalFee: Int,
    @SerializedName("amount_received") val amountReceived: Int,
    @SerializedName("pay_code") val payCode: String?,
    @SerializedName("checkout_url") val checkoutUrl: String,
    @SerializedName("status") val status: String,
    @SerializedName("expired_time") val expiredTime: Long,
    @SerializedName("order_items") val orderItems: List<OrderItem>
)
