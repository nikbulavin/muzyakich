import ru.resodostudio.muzyakich.MuzBuildType

plugins {
    alias(libs.plugins.muzyakich.android.application)
    alias(libs.plugins.muzyakich.android.application.compose)
    alias(libs.plugins.muzyakich.android.application.firebase)
    alias(libs.plugins.muzyakich.android.application.flavors)
    alias(libs.plugins.muzyakich.hilt)
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

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.locales)
    implementation(projects.core.mediaService)
    implementation(projects.core.model)

    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.tracing)
    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)
    implementation(libs.lottie.compose)

    debugImplementation(libs.androidx.compose.ui.testManifest)
}