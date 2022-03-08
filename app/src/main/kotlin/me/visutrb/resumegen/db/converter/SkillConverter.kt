package me.visutrb.resumegen.db.converter

import androidx.room.TypeConverter
import me.visutrb.resumegen.entity.Skill

class SkillConverter {

    @TypeConverter
    fun convertSkillToString(skill: Skill?): String? {
        return skill?.name
    }

    @TypeConverter
    fun convertStringToSkill(string: String?): Skill? {
        return string?.let { Skill(it) }
    }
}