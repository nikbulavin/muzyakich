package ru.resodostudio.muzyakich.ui.album.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.ui.album.AlbumScreen
import ru.resodostudio.muzyakich.ui.album.AlbumViewModel

fun EntryProviderScope<NavKey>.albumEntry(navigator: Navigator) {
    entry<AlbumNavKey> { albumKey ->
        AlbumScreen(
            onBackClick = navigator::goBack,
            viewModel = hiltViewModel<AlbumViewModel, AlbumViewModel.Factory> {
                it.create(albumKey.albumId)
            },
        )
    }
}