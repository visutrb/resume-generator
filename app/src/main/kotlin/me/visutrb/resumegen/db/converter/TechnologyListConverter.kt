package me.visutrb.resumegen.db.converter

import androidx.room.TypeConverter
import me.visutrb.resumegen.entity.Technology

class TechnologyListConverter {

    @TypeConverter
    fun toString(list: MutableList<Technology>?): String? {
        return list?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun fromString(string: String?): MutableList<Technology>? {
        return string?.split(",")?.map { Technology(it) }?.toMutableList()
    }
}