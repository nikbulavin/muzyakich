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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
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

include(":feature:album:detail:api")
include(":feature:album:detail:impl")
include(":feature:album:list:api")
include(":feature:album:list:impl")
include(":feature:artist:detail:api")
include(":feature:artist:detail:impl")
include(":feature:artist:list:api")
include(":feature:artist:list:impl")
include(":feature:library:api")
include(":feature:library:impl")
include(":feature:player:api")
include(":feature:player:impl")
include(":feature:playlist:detail:api")
include(":feature:playlist:detail:impl")
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
include(":feature:song:picker")
