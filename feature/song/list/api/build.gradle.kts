plugins {
    alias(libs.plugins.muzyakich.android.feature.api)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.song.list.api"
}

dependencies {
    api(projects.core.navigation)
}
