package me.visutrb.resumegen.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Education(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var school: String,
    var degree: String,
    var fos: String,
    var graduateYear: Int,
    var cgpa: Float,
    var resumeId: Int = 0
) : Parcelable
