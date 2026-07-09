plugins {
    alias(libs.plugins.muzyakich.android.feature.api)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.album.list.api"
}

dependencies {
    api(projects.core.navigation)
}
