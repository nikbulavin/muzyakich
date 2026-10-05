plugins {
    alias(libs.plugins.muzyakich.android.feature.impl)
    alias(libs.plugins.muzyakich.android.library.compose)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.song.editor.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.locales)
    implementation(projects.core.model)

    implementation(projects.feature.song.editor.api)

    implementation(platform(libs.coil.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.coil.compose)
}
