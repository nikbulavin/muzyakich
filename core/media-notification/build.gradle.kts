plugins {
    alias(libs.plugins.muzyakich.android.library)
    alias(libs.plugins.muzyakich.hilt)
}

android {
    namespace = "ru.resodostudio.muzyakich.core.media.notification"
}

dependencies {
    implementation(libs.androidx.media3.session)
}
