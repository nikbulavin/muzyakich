import ru.resodostudio.muzyakich.MuzBuildType

plugins {
    alias(libs.plugins.muzyakich.android.application)
    alias(libs.plugins.muzyakich.android.application.compose)
    alias(libs.plugins.muzyakich.android.application.firebase)
    alias(libs.plugins.muzyakich.android.application.flavors)
    alias(libs.plugins.muzyakich.hilt)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.kotlin.serialization)
}

android {
    defaultConfig {
        applicationId = "ru.resodostudio.muzyakich"
        versionCode = 6
        versionName = "1.0.0-beta01"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        androidResources.localeFilters += setOf("en", "ko", "ru")
    }
    buildTypes {
        debug {
            applicationIdSuffix = MuzBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            applicationIdSuffix = MuzBuildType.RELEASE.applicationIdSuffix
            signingConfig = signingConfigs.named("debug").get()
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    androidResources {
        generateLocaleConfig = true
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    namespace = "ru.resodostudio.muzyakich"
}

baselineProfile {
    dexLayoutOptimization = true

    variants {
        register("prodRelease") {
            automaticGenerationDuringBuild = true
        }
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)
    implementation(projects.core.locales)
    implementation(projects.core.mediaService)
    implementation(projects.core.model)
    implementation(projects.core.navigation)
    implementation(projects.core.ui)

    implementation(projects.feature.player.api)
    implementation(projects.feature.player.impl)
    implementation(projects.feature.playlist.detail.api)
    implementation(projects.feature.playlist.detail.impl)
    implementation(projects.feature.playlist.editor.api)
    implementation(projects.feature.playlist.editor.impl)
    implementation(projects.feature.playlist.list.api)
    implementation(projects.feature.playlist.list.impl)
    implementation(projects.feature.settings.api)
    implementation(projects.feature.settings.impl)
    implementation(projects.feature.song.detail.api)
    implementation(projects.feature.song.detail.impl)
    implementation(projects.feature.song.list.api)
    implementation(projects.feature.song.list.impl)

    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewModelNavigation3)
    implementation(libs.androidx.media3.ui.compose.material3)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing)
    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)
    implementation(libs.haze)
    implementation(libs.haze.materials)
    implementation(libs.lottie.compose)
    implementation(libs.play.app.update)
    implementation(libs.play.app.update.ktx)

    debugImplementation(libs.androidx.compose.ui.testManifest)

    baselineProfile(projects.baselineprofile)
}