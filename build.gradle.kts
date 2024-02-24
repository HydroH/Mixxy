// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    kotlin("jvm") version "2.0.0-Beta4" apply false
    kotlin("android") version "2.0.0-Beta4" apply false
    kotlin("plugin.serialization") version "2.0.0-Beta4" apply false
    id("com.android.application") version "8.4.0-alpha11" apply false
    id("com.android.library") version "8.4.0-alpha11" apply false
    id("com.google.devtools.ksp") version "2.0.0-Beta4-1.0.17" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.5" apply false
}
