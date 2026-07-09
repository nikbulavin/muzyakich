plugins {
    alias(libs.plugins.muzyakich.android.feature.impl)
    alias(libs.plugins.muzyakich.android.library.compose)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.artist.detail.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.mediaService)

    implementation(projects.feature.artist.detail.api)
    implementation(projects.feature.song.detail.api)

    implementation(libs.androidx.media3.exoPlayer)

    implementation(libs.androidx.navigation3.ui)
}
