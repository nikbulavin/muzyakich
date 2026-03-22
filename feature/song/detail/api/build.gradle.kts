plugins {
    alias(libs.plugins.muzyakich.android.feature.api)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.song.detail.api"
}

dependencies {
    api(projects.core.navigation)
}
