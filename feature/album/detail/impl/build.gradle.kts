plugins {
    alias(libs.plugins.muzyakich.android.feature.impl)
    alias(libs.plugins.muzyakich.android.library.compose)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.album.detail.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.mediaService)

    implementation(projects.feature.album.detail.api)
    implementation(projects.feature.song.detail.api)

    implementation(libs.androidx.media3.exoPlayer)
    implementation(libs.androidx.navigation3.ui)
    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)
    implementation(libs.haze)
    implementation(libs.haze.blur)
    implementation(libs.haze.blur.materials)
}
