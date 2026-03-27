package ru.resodostudio.muzyakich.feature.playlist.detail.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.playlist.detail.api.PlaylistNavKey
import ru.resodostudio.muzyakich.feature.playlist.detail.impl.PlaylistScreen
import ru.resodostudio.muzyakich.feature.playlist.detail.impl.PlaylistViewModel
import ru.resodostudio.muzyakich.feature.song.detail.api.navigateToSong

fun EntryProviderScope<NavKey>.playlistEntry(navigator: Navigator) {
    entry<PlaylistNavKey> { key ->
        PlaylistScreen(
            onBackClick = navigator::goBack,
            onSongMenuClick = navigator::navigateToSong,
            viewModel = hiltViewModel<PlaylistViewModel, PlaylistViewModel.Factory> {
                it.create(key.playlistUuid)
            },
        )
    }
}
