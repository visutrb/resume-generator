package me.visutrb.resumegen.db.dto

import androidx.room.Embedded
import me.visutrb.resumegen.entity.Project

data class ProjectWithTechnologies(
    @Embedded val project: Project,
//
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "projectId"
//    )
//    val technologies: List<Technology>
)
