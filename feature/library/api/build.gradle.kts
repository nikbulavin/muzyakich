plugins {
    alias(libs.plugins.muzyakich.android.feature.api)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.library.api"
}

dependencies {
    api(projects.core.navigation)
}
