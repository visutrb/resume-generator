package me.visutrb.resumegen.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Project(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String = "",
    var teamSize: Int = 0,
    var summary: String = "",
    var role: String = "",
    var resumeId: Int = 0,
    @Embedded var technologiesHolder: TechnologiesHolder = TechnologiesHolder()
) : Parcelable
