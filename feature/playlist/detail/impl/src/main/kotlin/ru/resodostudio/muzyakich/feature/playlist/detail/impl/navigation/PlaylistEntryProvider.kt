package ru.resodostudio.muzyakich.feature.playlist.detail.impl.navigation

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.metadata
import androidx.navigation3.ui.NavDisplay
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.playlist.detail.api.PlaylistNavKey
import ru.resodostudio.muzyakich.feature.playlist.detail.impl.PlaylistScreen
import ru.resodostudio.muzyakich.feature.playlist.detail.impl.PlaylistViewModel
import ru.resodostudio.muzyakich.feature.playlist.editor.api.navigateToPlaylistEditor
import ru.resodostudio.muzyakich.feature.song.detail.api.navigateToSong

fun EntryProviderScope<NavKey>.playlistEntry(
    navigator: Navigator,
    animSpec: FiniteAnimationSpec<Float>,
) {
    entry<PlaylistNavKey>(
        metadata = metadata {
            put(NavDisplay.TransitionKey) {
                fadeIn(animSpec) togetherWith fadeOut(animSpec)
            }
            put(NavDisplay.PopTransitionKey) {
                fadeIn(animSpec) togetherWith fadeOut(animSpec)
            }
            put(NavDisplay.PredictivePopTransitionKey) {
                fadeIn(animSpec) togetherWith fadeOut(animSpec)
            }
        },
    ) { key ->
        PlaylistScreen(
            onBackClick = navigator::goBack,
            onSongMenuClick = navigator::navigateToSong,
            onPlaylistEdit = navigator::navigateToPlaylistEditor,
            viewModel = hiltViewModel<PlaylistViewModel, PlaylistViewModel.Factory> {
                it.create(key.playlistUuid)
            },
        )
    }
}
