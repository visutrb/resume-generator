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
) : Parcelable {

    val addressLine1: String
        get() = street

    val addressLine2: String
        get() = if (state.isEmpty() || state.isBlank()) {
            "$city, $country $postalCode"
        } else {
            "$city, $state, $country $postalCode"
        }
}
