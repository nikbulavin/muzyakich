plugins {
    alias(libs.plugins.muzyakich.android.feature.api)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.playlist.detail.api"
}

dependencies {
    api(projects.core.navigation)
}
