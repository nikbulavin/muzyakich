plugins {
    alias(libs.plugins.muzyakich.android.feature.impl)
    alias(libs.plugins.muzyakich.android.library.compose)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.library.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.mediaService)
    implementation(projects.core.navigation)

    implementation(projects.feature.library.api)
    implementation(projects.feature.album.detail.api)
    implementation(projects.feature.album.list.api)
    implementation(projects.feature.album.list.impl)
    implementation(projects.feature.artist.detail.api)
    implementation(projects.feature.artist.list.api)
    implementation(projects.feature.artist.list.impl)
    implementation(projects.feature.playlist.detail.api)
    implementation(projects.feature.playlist.list.api)
    implementation(projects.feature.playlist.list.impl)
    implementation(projects.feature.settings.api)
    implementation(projects.feature.song.detail.api)
    implementation(projects.feature.song.list.api)
    implementation(projects.feature.song.list.impl)

    implementation(libs.androidx.media3.exoPlayer)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.play.app.update)
    implementation(libs.play.app.update.ktx)
}
