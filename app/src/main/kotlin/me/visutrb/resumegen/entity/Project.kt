package me.visutrb.resumegen.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Project(
    @PrimaryKey val id: Long = 0,
    val name: String,
    val teamSize: Int,
    val summary: String,
    val role: String,
    val resumeId: Long = 0
) : Parcelable
