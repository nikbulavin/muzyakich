plugins {
    alias(libs.plugins.muzyakich.android.library)
    alias(libs.plugins.muzyakich.android.room)
    alias(libs.plugins.muzyakich.hilt)
}

android {
    namespace = "ru.resodostudio.muzyakich.core.database"
}

dependencies {
    api(projects.core.model)
}
