package ru.resodostudio.muzyakich.core.data.repository.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import javax.inject.Inject

internal class AppLocaleManagerImpl @Inject constructor() : AppLocaleManager {

    override fun getCurrentLanguage(): String {
        return AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag() ?: ""
    }

    override fun setAppLanguage(languageTag: String) {
        runCatching {
            val localeList = if (languageTag.isEmpty()) {
                LocaleListCompat.getEmptyLocaleList()
            } else {
                LocaleListCompat.forLanguageTags(languageTag)
            }
            AppCompatDelegate.setApplicationLocales(localeList)
        }.onFailure { exception ->
            exception.printStackTrace()
        }
    }
}