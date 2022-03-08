package me.visutrb.resumegen.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
data class WorkExperience(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var companyName: String = "",
    var role: String = "",
    var startMonth: Int = -1,
    var startYear: Int = -1,
    var endMonth: Int = -1,
    var endYear: Int = -1,
    var resumeId: Int = 0,
    @Ignore val uuid: String = UUID.randomUUID().toString()
) : Parcelable {

    val isCurrentlyEmployed: Boolean
        get() = this.endMonth == -1 || this.endYear == -1

}
