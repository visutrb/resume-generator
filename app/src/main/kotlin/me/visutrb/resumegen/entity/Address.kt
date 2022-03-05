package me.visutrb.resumegen.entity

import androidx.room.Entity

data class Address(
    val street: String,
    val country: String,
    val state: String,
    val city: String,
    val postalCode: String
)
