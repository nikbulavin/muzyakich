plugins {
    alias(libs.plugins.muzyakich.android.feature.impl)
    alias(libs.plugins.muzyakich.android.library.compose)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.song.detail.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.locales)
    implementation(projects.core.mediaService)

    implementation(projects.feature.song.detail.api)

    implementation(platform(libs.coil.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.coil.compose)
}