plugins {
    alias(libs.plugins.muzyakich.android.library)
    alias(libs.plugins.muzyakich.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.resodostudio.muzyakich.core.navigation"
}

dependencies {
    api(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewModelNavigation3)
    implementation(libs.androidx.navigation3.ui)
}