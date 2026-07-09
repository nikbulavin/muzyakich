plugins {
    alias(libs.plugins.muzyakich.android.feature.impl)
    alias(libs.plugins.muzyakich.android.library.compose)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.album.list.impl"
}

dependencies {
    implementation(projects.core.data)

    implementation(projects.feature.album.list.api)

    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)

    implementation(libs.androidx.navigation3.ui)
}
