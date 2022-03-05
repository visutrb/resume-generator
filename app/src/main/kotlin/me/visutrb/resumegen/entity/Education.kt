package me.visutrb.resumegen.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Education(
    @PrimaryKey val id: Int,
    val classRank: String,
    val passingYear: Int,
    val cgpa: String,
    val resumeId: Int
)
