package me.visutrb.resumegen.db.dto

import androidx.room.Embedded
import androidx.room.Relation
import me.visutrb.resumegen.entity.Project
import me.visutrb.resumegen.entity.Technology

data class ProjectWithTechnologies(
    @Embedded val project: Project,

    @Relation(
        parentColumn = "id",
        entityColumn = "projectId"
    )
    val technologies: List<Technology>
)
