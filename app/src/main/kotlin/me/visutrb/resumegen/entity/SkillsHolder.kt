package me.visutrb.resumegen.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SkillsHolder(
    var skills: MutableList<Skill> = mutableListOf()
) : Parcelable