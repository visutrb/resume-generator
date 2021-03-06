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
data class Resume(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var firstName: String = "",
    var middleName: String? = null,
    var lastName: String = "",
    var profilePicturePath: String = "",
    var phoneNumber: String = "",
    var emailAddress: String = "",
    @Embedded var address: Address = Address(),
    var role: String = "",
    var careerObjective: String = "",
    var totalYearsOfExp: Int = -1,
    var createDate: Date = Date(),
    var updateDate: Date = Date(),
    @Embedded var skillsHolder: SkillsHolder = SkillsHolder(),
    @Ignore val uuid: String = UUID.randomUUID().toString()
) : Parcelable {

    val fullName: String
        get() {
            return if (middleName?.isNotBlank() == true || middleName?.isNotEmpty() == true) {
                "$firstName $middleName $lastName"
            } else {
                "$firstName $lastName"
            }
        }
}
