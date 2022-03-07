package me.visutrb.resumegen.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.visutrb.resumegen.db.converter.*
import me.visutrb.resumegen.db.dao.EducationDao
import me.visutrb.resumegen.db.dao.ProjectDao
import me.visutrb.resumegen.db.dao.ResumeDao
import me.visutrb.resumegen.db.dao.WorkExperienceDao
import me.visutrb.resumegen.entity.Education
import me.visutrb.resumegen.entity.Project
import me.visutrb.resumegen.entity.Resume
import me.visutrb.resumegen.entity.WorkExperience

@Database(
    entities = [
        Education::class,
        Project::class,
        Resume::class,
        WorkExperience::class
    ],
    version = 1
)
@TypeConverters(
    DateConverter::class,
    SkillsHolderConverter::class,
    SkillConverter::class,
    SkillListConverter::class,
    TechnologiesHolderConverter::class,
    TechnologyListConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun educationDao(): EducationDao
    abstract fun projectDao(): ProjectDao
    abstract fun resumeDao(): ResumeDao
    abstract fun workExperienceDao(): WorkExperienceDao
}