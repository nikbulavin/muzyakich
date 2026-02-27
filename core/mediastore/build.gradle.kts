plugins {
    alias(libs.plugins.muzyakich.android.library)
    alias(libs.plugins.muzyakich.hilt)
}

android {
    namespace = "ru.resodostudio.muzyakich.core.mediastore"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
}