package ru.resodostudio.muzyakich.feature.album.detail.impl.navigation

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
import ru.resodostudio.muzyakich.feature.album.detail.api.AlbumNavKey
import ru.resodostudio.muzyakich.feature.album.detail.impl.AlbumScreen
import ru.resodostudio.muzyakich.feature.album.detail.impl.AlbumViewModel
import ru.resodostudio.muzyakich.feature.song.detail.api.navigateToSong

fun EntryProviderScope<NavKey>.albumEntry(
    navigator: Navigator,
    animSpec: FiniteAnimationSpec<Float>,
) {
    entry<AlbumNavKey>(
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
