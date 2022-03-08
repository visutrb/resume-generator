package me.visutrb.resumegen

import android.app.Application
import me.visutrb.resumegen.di.databaseModule
import me.visutrb.resumegen.di.resumeFormModule
import me.visutrb.resumegen.di.resumeListModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(databaseModule, resumeListModule, resumeFormModule)
        }
    }
}