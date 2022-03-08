package me.visutrb.resumegen.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TechnologiesHolder(
    var technologies: MutableList<Technology> = mutableListOf()
) : Parcelable
