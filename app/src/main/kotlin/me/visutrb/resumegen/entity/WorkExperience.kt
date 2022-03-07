package me.visutrb.resumegen.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class WorkExperience(
    @PrimaryKey var id: Int = 0,
    var companyName: String,
    var role: String,
    var startMonth: Int,
    var startYear: Int,
    var endMonth: Int = -1,
    var endYear: Int = -1,
    var resumeId: Int = 0
) : Parcelable
