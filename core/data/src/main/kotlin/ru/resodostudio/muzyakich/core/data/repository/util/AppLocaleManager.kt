package ru.resodostudio.muzyakich.core.data.repository.util

interface AppLocaleManager {

    fun getCurrentLanguage(): String

    fun setAppLanguage(languageTag: String)
}