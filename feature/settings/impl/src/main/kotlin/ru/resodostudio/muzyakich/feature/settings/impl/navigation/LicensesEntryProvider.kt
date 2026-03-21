package ru.resodostudio.muzyakich.feature.settings.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.settings.api.LicensesNavKey
import ru.resodostudio.muzyakich.feature.settings.impl.LicensesScreen

fun EntryProviderScope<NavKey>.licensesEntry(navigator: Navigator) {
    entry<LicensesNavKey> {
        LicensesScreen(
            onBackClick = navigator::goBack,
        )
    }
}