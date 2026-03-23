package ru.resodostudio.muzyakich.feature.playlist.editor.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.playlist.editor.api.PlaylistEditorNavKey
import ru.resodostudio.muzyakich.feature.playlist.editor.impl.PlaylistEditorScreen
import ru.resodostudio.muzyakich.feature.playlist.editor.impl.PlaylistEditorViewModel

fun EntryProviderScope<NavKey>.playlistEditorEntry(navigator: Navigator) {
    entry<PlaylistEditorNavKey> { key ->
        PlaylistEditorScreen(
            onBackClick = navigator::goBack,
            viewModel = hiltViewModel<PlaylistEditorViewModel, PlaylistEditorViewModel.Factory> {
                it.create(key.playlistUuid)
            },
        )
    }
}
