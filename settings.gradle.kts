@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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
            url = uri("https://jitpack.io")
        }
        maven {
            url = uri("https://maven.pkg.github.com/samunohito/mfm.kt")
            credentials {
                username = extra.properties["gpr.user"] as String? ?: System.getenv("GPR_USER")
                password = extra.properties["gpr.key"] as String ? ?: System.getenv("GPR_API_KEY")
            }
        }
    }
}

rootProject.name = "Mixxy"
include(":app")
include(":mfm-compose")
