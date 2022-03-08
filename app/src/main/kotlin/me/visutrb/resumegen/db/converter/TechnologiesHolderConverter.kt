package me.visutrb.resumegen.db.converter

import androidx.room.TypeConverter
import me.visutrb.resumegen.entity.TechnologiesHolder
import me.visutrb.resumegen.entity.Technology

class TechnologiesHolderConverter {

    @TypeConverter
    fun toString(value: TechnologiesHolder?): String? {
        return value?.technologies?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun fromString(string: String?): TechnologiesHolder? {
        val technologies = string?.split(",")?.map { Technology(it) }?.toMutableList()
        return technologies?.let { TechnologiesHolder(it) }
    }
}