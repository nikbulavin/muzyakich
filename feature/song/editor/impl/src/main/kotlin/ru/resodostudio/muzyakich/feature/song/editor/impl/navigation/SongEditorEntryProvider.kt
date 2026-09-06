package ru.resodostudio.muzyakich.feature.song.editor.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.song.editor.api.SongEditorNavKey
import ru.resodostudio.muzyakich.feature.song.editor.impl.SongEditorScreen
import ru.resodostudio.muzyakich.feature.song.editor.impl.SongEditorViewModel

fun EntryProviderScope<NavKey>.songEditorEntry(navigator: Navigator) {
    entry<SongEditorNavKey> { key ->
        SongEditorScreen(
            onBackClick = navigator::goBack,
            viewModel = hiltViewModel<SongEditorViewModel, SongEditorViewModel.Factory> {
                it.create(key.mediaId)
            },
        )
    }
}
