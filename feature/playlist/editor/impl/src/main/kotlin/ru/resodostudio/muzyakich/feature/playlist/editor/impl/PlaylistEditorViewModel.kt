package ru.resodostudio.muzyakich.feature.playlist.editor.impl

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.resodostudio.muzyakich.core.data.repository.PlaylistsRepository
import ru.resodostudio.muzyakich.core.model.data.Song
import kotlin.uuid.Uuid

@HiltViewModel(assistedFactory = PlaylistEditorViewModel.Factory::class)
internal class PlaylistEditorViewModel @AssistedInject constructor(
    @Assisted val playlistUuid: Uuid?,
    private val playlistsRepository: PlaylistsRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            playlistUuid: Uuid?,
        ): PlaylistEditorViewModel
    }
}

sealed interface PlaylistEditorUiState {

    data object Loading : PlaylistEditorUiState

    data class Success(
        val name: String,
        val coverFileName: String?,
        val songs: List<Song>,
    ) : PlaylistEditorUiState
}
