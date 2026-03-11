plugins {
    alias(libs.plugins.muzyakich.android.library)
    alias(libs.plugins.muzyakich.hilt)
}

android {
    namespace = "ru.resodostudio.muzyakich.core.media.service"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.locales)
    implementation(projects.core.model)
    implementation(projects.core.mediaNotification)

    implementation(libs.androidx.media3.cast)
    implementation(libs.androidx.media3.exoPlayer)
    implementation(libs.androidx.media3.session)
    implementation(libs.kotlinx.coroutines.guava)
}
