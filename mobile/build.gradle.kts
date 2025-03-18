import ru.resodostudio.muzyakich.MuzBuildType

plugins {
    alias(libs.plugins.muzyakich.android.application)
    alias(libs.plugins.muzyakich.android.application.compose)
    alias(libs.plugins.muzyakich.android.application.firebase)
    alias(libs.plugins.muzyakich.android.application.flavors)
}

android {
    defaultConfig {
        applicationId = "ru.resodostudio.muzyakich"
        versionCode = 1
        versionName = "1.0.0-alpha01"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }
    namespace = "ru.resodostudio.muzyakich"
}

dependencies {
    implementation(projects.core.designsystem)

    implementation(libs.androidx.activity.compose)

    debugImplementation(libs.androidx.compose.ui.testManifest)
}