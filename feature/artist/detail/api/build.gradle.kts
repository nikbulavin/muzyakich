plugins {
    alias(libs.plugins.muzyakich.android.feature.api)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.artist.detail.api"
}

dependencies {
    api(projects.core.navigation)
}
