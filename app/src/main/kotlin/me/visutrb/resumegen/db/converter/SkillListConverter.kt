package me.visutrb.resumegen.db.converter

import androidx.room.TypeConverter
import me.visutrb.resumegen.entity.Skill

class SkillListConverter {

    @TypeConverter
    fun convertListToString(skills: MutableList<Skill>?): String? {
        return skills?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun convertStringToList(string: String?): MutableList<Skill>? {
        return string?.split(",")?.map { Skill(it) }?.toMutableList()
    }
}