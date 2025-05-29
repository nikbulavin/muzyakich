pluginManagement {
    includeBuild("build-logic")
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
    }
}

rootProject.name = "muzyakich"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":mobile")

include(":core:common")
include(":core:data")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:designsystem")
include(":core:locales")
include(":core:media-notification")
include(":core:media-service")
include(":core:mediastore")
include(":core:model")