package me.visutrb.resumegen.db.converter

import androidx.room.TypeConverter
import me.visutrb.resumegen.entity.Skill
import me.visutrb.resumegen.entity.SkillsHolder

class SkillsHolderConverter {

    @TypeConverter
    fun convertSkillsHolderToString(skillsHolder: SkillsHolder?): String? {
        return skillsHolder?.skills?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun convertStringToSkillsHolder(string: String?): SkillsHolder? {
        val skills = string?.split(",")?.map { Skill(it) }?.toMutableList()
        return skills?.let { SkillsHolder(skills) }
    }
}