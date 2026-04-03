plugins {
    alias(libs.plugins.muzyakich.android.feature.impl)
    alias(libs.plugins.muzyakich.android.library.compose)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.playlist.detail.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.locales)
    implementation(projects.core.mediaService)

    implementation(projects.feature.playlist.detail.api)
    implementation(projects.feature.playlist.editor.api)
    implementation(projects.feature.song.detail.api)

    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)

    implementation(libs.androidx.media3.exoPlayer)
    implementation(libs.androidx.navigation3.ui)
}