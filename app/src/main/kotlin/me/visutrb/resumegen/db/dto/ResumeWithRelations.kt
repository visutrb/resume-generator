package me.visutrb.resumegen.db.dto

import androidx.room.Embedded
import androidx.room.Relation
import me.visutrb.resumegen.entity.Education
import me.visutrb.resumegen.entity.Project
import me.visutrb.resumegen.entity.Resume
import me.visutrb.resumegen.entity.WorkExperience

data class ResumeWithRelations(
    @Embedded val resume: Resume,

    @Relation(
        parentColumn = "id",
        entityColumn = "resumeId"
    )
    val workExperiences: List<WorkExperience>,

    @Relation(
        parentColumn = "id",
        entityColumn = "resumeId"
    )
    val educations: List<Education>,

    @Relation(
        entity = Project::class,
        parentColumn = "id",
        entityColumn = "resumeId"
    )
    val projectsWithTechnologies: List<ProjectWithTechnologies>
)
