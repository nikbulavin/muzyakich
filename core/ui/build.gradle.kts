plugins {
    alias(libs.plugins.muzyakich.android.library)
    alias(libs.plugins.muzyakich.android.library.compose)
}

android {
    namespace = "ru.resodostudio.muzyakich.core.ui"
}

dependencies {
    api(projects.core.designsystem)
    api(projects.core.locales)
    api(projects.core.model)
    implementation(projects.core.common)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.lottie.compose)
    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)
}
