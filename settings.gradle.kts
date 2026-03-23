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

include(":baselineprofile")

include(":core:common")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:designsystem")
include(":core:domain")
include(":core:locales")
include(":core:media-notification")
include(":core:media-service")
include(":core:mediastore")
include(":core:model")
include(":core:navigation")
include(":core:ui")

include(":feature:player:api")
include(":feature:player:impl")
include(":feature:playlist:editor:api")
include(":feature:playlist:editor:impl")
include(":feature:playlist:list:api")
include(":feature:playlist:list:impl")
include(":feature:settings:api")
include(":feature:settings:impl")
include(":feature:song:detail:api")
include(":feature:song:detail:impl")
include(":feature:song:list:api")
include(":feature:song:list:impl")
