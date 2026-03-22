package ru.resodostudio.muzyakich.feature.settings.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.settings.api.SettingsNavKey
import ru.resodostudio.muzyakich.feature.settings.api.navigateToLicenses
import ru.resodostudio.muzyakich.feature.settings.impl.SettingsScreen

fun EntryProviderScope<NavKey>.settingsEntry(navigator: Navigator) {
    entry<SettingsNavKey> {
        SettingsScreen(
            onBackClick = navigator::goBack,
            onLicensesClick = navigator::navigateToLicenses,
        )
    }
}