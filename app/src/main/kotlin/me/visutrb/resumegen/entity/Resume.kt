package me.visutrb.resumegen.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Resume(
    @PrimaryKey val id: Int,
    val firstName: String,
    val middleName: String? = null,
    val lastName: String,
    val profilePicturePath: String,
    val phoneNumber: String,
    val emailAddress: String,
    val careerObjective: String,
    val createDate: Date = Date(),
    val updateDate: Date = Date()
)
