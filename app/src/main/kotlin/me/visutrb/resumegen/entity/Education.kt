package me.visutrb.resumegen.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
data class Education(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var school: String = "",
    var degree: String = "",
    var fos: String = "",
    var graduateYear: Int = 0,
    var cgpa: Float = 0F,
    var resumeId: Int = 0,
    @Ignore val uuid: String = UUID.randomUUID().toString()
) : Parcelable
