plugins {
    alias(libs.plugins.muzyakich.android.feature.impl)
    alias(libs.plugins.muzyakich.android.library.compose)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.settings.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.locales)
    implementation(projects.core.mediaService)

    implementation(projects.feature.settings.api)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.browser)
}