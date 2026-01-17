plugins {
    alias(libs.plugins.muzyakich.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose)
}

android {
    namespace = "ru.resodostudio.muzyakich.core.navigation"
}

dependencies {
    api(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewModelNavigation3)
}