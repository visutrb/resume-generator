package me.visutrb.resumegen.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Technology(
    @PrimaryKey val id: Int,
    val name: String,
    val projectId: Int
)
