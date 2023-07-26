// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("com.android.application") version "8.1.0-rc01" apply false
    kotlin("android") version "1.8.20" apply false
    kotlin("jvm") version "1.8.20" apply false
    kotlin("plugin.serialization") version "1.8.20" apply false
    id("com.google.devtools.ksp") version "1.8.20-1.0.10" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}