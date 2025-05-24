package com.geby.shoepay

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun generateSignature(merchantCode: String, merchantRef: String, amount: Int, privateKey: String): String {
    val data = "$merchantCode$merchantRef$amount"

    val secretKey = SecretKeySpec(privateKey.toByteArray(charset("ISO-8859-1")), "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(secretKey)

    val hmacBytes = mac.doFinal(data.toByteArray(charset("ISO-8859-1")))
    return hmacBytes.joinToString("") { "%02x".format(it) }
}
