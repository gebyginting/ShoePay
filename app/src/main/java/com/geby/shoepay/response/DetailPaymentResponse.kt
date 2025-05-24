package com.geby.shoepay.response

import com.google.gson.annotations.SerializedName

data class DetailPaymentResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class InstructionsItem(

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("steps")
	val steps: List<String?>? = null
)

data class OrderItemsItem(

	@field:SerializedName("quantity")
	val quantity: Int? = null,

	@field:SerializedName("product_url")
	val productUrl: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("subtotal")
	val subtotal: Int? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("sku")
	val sku: String? = null
)

data class Data(

	@field:SerializedName("instructions")
	val instructions: List<InstructionsItem?>? = null,

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("fee_merchant")
	val feeMerchant: Int? = null,

	@field:SerializedName("checkout_url")
	val checkoutUrl: String? = null,

	@field:SerializedName("amount_received")
	val amountReceived: Int? = null,

	@field:SerializedName("pay_code")
	val payCode: String? = null,

	@field:SerializedName("customer_phone")
	val customerPhone: String? = null,

	@field:SerializedName("payment_selection_type")
	val paymentSelectionType: String? = null,

	@field:SerializedName("expired_time")
	val expiredTime: Int? = null,

	@field:SerializedName("pay_url")
	val payUrl: Any? = null,

	@field:SerializedName("reference")
	val reference: String? = null,

	@field:SerializedName("callback_url")
	val callbackUrl: String? = null,

	@field:SerializedName("paid_at")
	val paidAt: String? = null,

	@field:SerializedName("fee_customer")
	val feeCustomer: Int? = null,

	@field:SerializedName("customer_email")
	val customerEmail: String? = null,

	@field:SerializedName("total_fee")
	val totalFee: Int? = null,

	@field:SerializedName("merchant_ref")
	val merchantRef: String? = null,

	@field:SerializedName("return_url")
	val returnUrl: String? = null,

	@field:SerializedName("payment_name")
	val paymentName: String? = null,

	@field:SerializedName("customer_name")
	val customerName: String? = null,

	@field:SerializedName("payment_method")
	val paymentMethod: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("qr_url")
	val qrUrl: String? = null,

	@field:SerializedName("order_items")
	val orderItems: List<OrderItemsItem?>? = null
)
