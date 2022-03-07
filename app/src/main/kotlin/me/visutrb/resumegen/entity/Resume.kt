package me.visutrb.resumegen.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Resume(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var firstName: String = "",
    var middleName: String? = null,
    var lastName: String = "",
    var profilePicturePath: String = "",
    var phoneNumber: String = "",
    var emailAddress: String = "",
    @Embedded var address: Address = Address(),
    var careerObjective: String = "",
    var totalYearsOfExp: Int = -1,
    var createDate: Date = Date(),
    var updateDate: Date = Date(),
    @Embedded var skillsHolder: SkillsHolder = SkillsHolder()
) {

    val fullName: String
        get() {
            return if (middleName?.isNotBlank() == true || middleName?.isNotEmpty() == true) {
                "$firstName $middleName $lastName"
            } else {
                "$firstName $lastName"
            }
        }
}
