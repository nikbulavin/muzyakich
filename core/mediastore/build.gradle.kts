plugins {
    alias(libs.plugins.muzyakich.android.library)
    alias(libs.plugins.muzyakich.hilt)
}

android {
    namespace = "ru.resodostudio.muzyakich.core.data"
}

dependencies {
    implementation(projects.core.model)
}