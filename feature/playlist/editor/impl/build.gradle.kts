plugins {
    alias(libs.plugins.muzyakich.android.feature.impl)
    alias(libs.plugins.muzyakich.android.library.compose)
}

android {
    namespace = "ru.resodostudio.muzyakich.feature.playlist.editor.impl"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.locales)

    implementation(projects.feature.playlist.editor.api)
    implementation(projects.feature.song.list.api)
    implementation(projects.feature.song.picker)

    implementation(libs.androidx.activity.compose)
    implementation(libs.reorderable)

    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)
}