import ru.resodostudio.muzyakich.configureFlavors

plugins {
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.muzyakich.android.test)
}

android {
    namespace = "ru.resodostudio.muzyakich.baselineprofile"

    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.enabledRules"] = "BaselineProfile"

        buildConfigField("String", "APP_BUILD_TYPE_SUFFIX", "\"\"")
    }

    buildFeatures {
        buildConfig = true
    }

    configureFlavors(this) { flavor ->
        buildConfigField(
            "String",
            "APP_FLAVOR_SUFFIX",
            "\"${flavor.applicationIdSuffix ?: ""}\""
        )
    }

    testOptions.managedDevices.localDevices {
        create("pixel6Api35") {
            device = "Pixel 6"
            apiLevel = 35
            systemImageSource = "aosp"
        }
    }

    targetProjectPath = ":mobile"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

baselineProfile {
    managedDevices.clear()
    managedDevices += "pixel6Api35"
    useConnectedDevices = false
}

dependencies {
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.espresso.core)
    implementation(libs.androidx.test.ext)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.test.runner)
    implementation(libs.androidx.test.uiautomator)
}