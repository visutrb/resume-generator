package me.visutrb.resumegen.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class WorkExperience(
    @PrimaryKey val id: Long = 0,
    val companyName: String,
    val role: String,
    val startMonth: Int,
    val startYear: Int,
    val endMonth: Int = -1,
    val endYear: Int = -1,
    val resumeId: Long = 0
) : Parcelable
