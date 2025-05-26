plugins {
    alias(libs.plugins.muzyakich.android.library)
}

android {
    namespace = "ru.resodostudio.muzyakich.core.model"
}

dependencies {
    implementation(projects.core.common)
}