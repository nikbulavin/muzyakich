package ru.resodostudio.muzyakich.ui.player.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.ui.player.PlayerScreen

fun EntryProviderScope<NavKey>.playerEntry(navigator: Navigator) {
    entry<PlayerNavKey> {
        PlayerScreen(
            onBackClick = navigator::goBack,
        )
    }
}