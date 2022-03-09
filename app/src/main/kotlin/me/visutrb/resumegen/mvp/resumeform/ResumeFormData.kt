package me.visutrb.resumegen.mvp.resumeform

import me.visutrb.resumegen.entity.Education
import me.visutrb.resumegen.entity.Project
import me.visutrb.resumegen.entity.Resume
import me.visutrb.resumegen.entity.WorkExperience

data class ResumeFormData(
    val resume: Resume? = null,
    val educations: List<Education> = listOf(),
    val workExperiences: List<WorkExperience> = listOf(),
    val projects: List<Project> = listOf()
)