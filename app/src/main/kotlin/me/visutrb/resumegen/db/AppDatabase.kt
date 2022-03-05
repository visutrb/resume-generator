package me.visutrb.resumegen.db

import androidx.room.Database
import androidx.room.RoomDatabase
import me.visutrb.resumegen.db.dao.*
import me.visutrb.resumegen.entity.*

@Database(
    entities = [
        Education::class,
        Project::class,
        Resume::class,
        Skill::class,
        Technology::class,
        WorkExperience::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun educationDao(): EducationDao
    abstract fun projectDao(): ProjectDao
    abstract fun resumeDao(): ResumeDao
    abstract fun skillDao(): SkillDao
    abstract fun technologyDao(): TechnologyDao
    abstract fun workExperienceDao(): WorkExperienceDao
}