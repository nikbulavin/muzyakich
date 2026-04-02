package ru.resodostudio.muzyakich.ui.album.detail.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.song.detail.api.navigateToSong
import ru.resodostudio.muzyakich.ui.album.detail.AlbumScreen
import ru.resodostudio.muzyakich.ui.album.detail.AlbumViewModel

fun EntryProviderScope<NavKey>.albumEntry(navigator: Navigator) {
    entry<AlbumNavKey>(
        metadata = NavDisplay.transitionSpec { fadeIn() togetherWith fadeOut() } +
                NavDisplay.popTransitionSpec { fadeIn() togetherWith fadeOut() } +
                NavDisplay.predictivePopTransitionSpec { fadeIn() togetherWith fadeOut() },
    ) { albumKey ->
        AlbumScreen(
            onBackClick = navigator::goBack,
            onSongMenuClick = navigator::navigateToSong,
            viewModel = hiltViewModel<AlbumViewModel, AlbumViewModel.Factory> {
                it.create(albumKey.albumId)
            },
        )
    }
}