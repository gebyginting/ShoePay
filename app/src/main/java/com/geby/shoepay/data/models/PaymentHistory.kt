package com.geby.shoepay.data.models

data class PaymentHistory(
    var userId: String? = null,
    var username: String? = null,
    var email: String? = null,
    var payment_method: String? = null,
    var amount: Int? = null,
    var status: String? = null,
    var orderItem: String? = null,
    var orderQuantity: Int? = null
)
