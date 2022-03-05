package me.visutrb.resumegen.entity

import androidx.room.PrimaryKey

data class WorkExperience(
    @PrimaryKey val id: Int,
    val companyName: String,
    val startMonth: Int,
    val startYear: Int,
    val endMonth: Int = -1,
    val endYear: Int = 0
)
