package me.visutrb.resumegen.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    var street: String = "",
    var country: String = "",
    var state: String = "",
    var city: String = "",
    var postalCode: String = ""
) : Parcelable
