plugins {
    alias(libs.plugins.muzyakich.android.library)
    alias(libs.plugins.muzyakich.hilt)
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
    namespace = "ru.resodostudio.muzyakich.core.datastore"
}

dependencies {
    implementation(projects.core.datastoreProto)
    implementation(projects.core.model)
    implementation(projects.core.common)

    implementation(libs.androidx.dataStore)
}