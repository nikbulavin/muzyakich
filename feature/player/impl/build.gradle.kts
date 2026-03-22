plugins {
    alias(libs.plugins.muzyakich.android.feature.impl)
    alias(libs.plugins.muzyakich.android.library.compose)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.player.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.locales)
    implementation(projects.core.mediaService)

    implementation(projects.feature.player.api)

    implementation(platform(libs.coil.bom))
    implementation(libs.androidx.media3.cast)
    implementation(libs.androidx.media3.exoPlayer)
    implementation(libs.androidx.media3.ui.compose.material3)
    implementation(libs.coil.compose)
}