package com.geby.shoepay.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shoe (
    val shoeName: String,
    val shoePrice: Int,
    val shoeType: String,
    val shoeImage: String,
    var shoeQuantity: Int = 1
) : Parcelable