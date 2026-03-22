plugins {
    alias(libs.plugins.muzyakich.android.feature.impl)
    alias(libs.plugins.muzyakich.android.library.compose)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.song.list.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.locales)
    implementation(projects.core.mediaService)

    implementation(projects.feature.song.list.api)

    implementation(libs.androidx.media3.exoPlayer)
}