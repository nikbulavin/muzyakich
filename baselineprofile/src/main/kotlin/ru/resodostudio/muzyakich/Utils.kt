package ru.resodostudio.muzyakich

import ru.resodostudio.muzyakich.baselineprofile.BuildConfig

val PACKAGE_NAME = buildString {
    append("ru.resodostudio.muzyakich")
    append(BuildConfig.APP_FLAVOR_SUFFIX)
}