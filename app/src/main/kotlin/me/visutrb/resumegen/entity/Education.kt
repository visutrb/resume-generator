package me.visutrb.resumegen.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Education(
    @PrimaryKey val id: Long = 0,
    val school: String,
    val degree: String,
    val fos: String,
    val graduateYear: Int,
    val cgpa: Float,
    val resumeId: Long = 0
) : Parcelable
