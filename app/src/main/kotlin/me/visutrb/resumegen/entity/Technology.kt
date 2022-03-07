package me.visutrb.resumegen.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Technology(
    var name: String
) : Parcelable
