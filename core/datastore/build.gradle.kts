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
    implementation(projects.core.common)

    api(projects.core.datastoreProto)
    api(projects.core.model)

    api(libs.androidx.dataStore)
}