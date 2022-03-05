package me.visutrb.resumegen.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(
    @PrimaryKey val id: Int,
    val name: String,
    val teamSize: Int,
    val summary: String,
    val role: String,
    val resumeId: Int
)
