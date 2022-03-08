// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version androidPluginVersion apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
}

tasks.create("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}