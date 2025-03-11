package ru.resodostudio.muzyakich

enum class MuzBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}