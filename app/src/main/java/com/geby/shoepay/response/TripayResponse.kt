package com.geby.shoepay.response

import com.google.gson.annotations.SerializedName

data class TripayResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class FeeMerchant(

	@field:SerializedName("flat")
	val flat: Double? = null,

	@field:SerializedName("percent")
	val percent: Double? = null
)

data class FeeCustomer(

	@field:SerializedName("flat")
	val flat: Double? = null,

	@field:SerializedName("percent")
	val percent: Double? = null
)

data class DataItem(

	@field:SerializedName("icon_url")
	val iconUrl: String? = null,

	@field:SerializedName("fee_merchant")
	val feeMerchant: FeeMerchant? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("minimum_fee")
	val minimumFee: Int? = null,

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("maximum_amount")
	val maximumAmount: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("minimum_amount")
	val minimumAmount: Int? = null,

	@field:SerializedName("fee_customer")
	val feeCustomer: FeeCustomer? = null,

	@field:SerializedName("total_fee")
	val totalFee: TotalFee? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("maximum_fee")
	val maximumFee: Any? = null,

	@field:SerializedName("group")
	val group: String? = null
)

data class TotalFee(

	@field:SerializedName("flat")
	val flat: Double? = null,

	@field:SerializedName("percent")
	val percent: Double? = null
)
