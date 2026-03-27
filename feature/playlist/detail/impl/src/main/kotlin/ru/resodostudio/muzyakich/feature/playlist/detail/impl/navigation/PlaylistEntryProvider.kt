package ru.resodostudio.muzyakich.feature.playlist.detail.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.playlist.detail.api.PlaylistNavKey
import ru.resodostudio.muzyakich.feature.playlist.detail.impl.PlaylistScreen

fun EntryProviderScope<NavKey>.playlistEntry(navigator: Navigator) {
    entry<PlaylistNavKey> {
        PlaylistScreen(
            onBackClick = navigator::goBack,
        )
    }
}