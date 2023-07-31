pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://androidx.dev/storage/compose-compiler/repository/")
        }
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

rootProject.name = "Mixxy"
include(":app")
include(":client")
