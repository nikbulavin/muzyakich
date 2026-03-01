plugins {
    alias(libs.plugins.muzyakich.android.library)
}

android {
    namespace = "ru.resodostudio.muzyakich.core.domain"
}

dependencies {
    api(projects.core.data)
    api(projects.core.model)

    implementation(libs.javax.inject)
}