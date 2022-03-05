package me.visutrb.resumegen.di

import androidx.room.Room
import me.visutrb.resumegen.db.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "resume").build()
    }

    factory { get<AppDatabase>().educationDao() }
    factory { get<AppDatabase>().projectDao() }
    factory { get<AppDatabase>().resumeDao() }
    factory { get<AppDatabase>().skillDao() }
    factory { get<AppDatabase>().technologyDao() }
    factory { get<AppDatabase>().workExperienceDao() }
}