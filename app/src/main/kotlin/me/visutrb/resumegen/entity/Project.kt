package me.visutrb.resumegen.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
data class Project(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String = "",
    var teamSize: Int = 0,
    var summary: String = "",
    var role: String = "",
    var resumeId: Int = 0,
    @Embedded var technologiesHolder: TechnologiesHolder = TechnologiesHolder(),
    @Ignore val uuid: String = UUID.randomUUID().toString()
) : Parcelable
