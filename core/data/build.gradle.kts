plugins {
    alias(libs.plugins.muzyakich.android.library)
    alias(libs.plugins.muzyakich.hilt)
}

android {
    namespace = "ru.resodostudio.muzyakich.core.data"
}

dependencies {
    api(projects.core.common)
    api(projects.core.database)
    api(projects.core.datastore)
    implementation(projects.core.mediastore)

    implementation(libs.androidx.appcompat)
    implementation(libs.kotlinx.coroutines.playServices)
    implementation(libs.play.app.update)
    implementation(libs.play.app.update.ktx)
}
