package com.geby.shoepay.utilities

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.text.NumberFormat
import java.util.Locale

object Helper {
    fun rupiahFormat(amount: Int?): String {
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        format.maximumFractionDigits = 0
        return format.format(amount)
    }

    fun generateQrCode(data: String): Bitmap? {
        return try {
            val barcodeEncoder = BarcodeEncoder()
            barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 400, 400)
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    fun formatRupiah(amount: Int): String {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(amount).replace(",00", "") // untuk hilangkan ,00
    }
}