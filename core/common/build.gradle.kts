plugins {
    alias(libs.plugins.muzyakich.jvm.library)
    alias(libs.plugins.muzyakich.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}