plugins {
    alias(libs.plugins.muzyakich.android.feature.impl)
    alias(libs.plugins.muzyakich.android.library.compose)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.artist.list.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)

    implementation(projects.feature.artist.list.api)

    implementation(libs.androidx.navigation3.ui)
}
