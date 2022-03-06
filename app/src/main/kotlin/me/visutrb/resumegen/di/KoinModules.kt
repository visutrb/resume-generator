package me.visutrb.resumegen.di

import androidx.room.Room
import me.visutrb.resumegen.db.AppDatabase
import me.visutrb.resumegen.mvp.resumelist.ResumeListActivity
import me.visutrb.resumegen.mvp.resumelist.ResumeListPresenter
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

val resumeListModule = module {
    scope<ResumeListActivity> {
        scoped { ResumeListPresenter(get()) }
    }
}