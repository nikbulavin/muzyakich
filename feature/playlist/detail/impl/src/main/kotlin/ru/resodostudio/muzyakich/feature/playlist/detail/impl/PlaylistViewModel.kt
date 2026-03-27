package ru.resodostudio.muzyakich.feature.playlist.detail.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.data.repository.PlaylistsRepository
import ru.resodostudio.muzyakich.core.model.data.Playlist
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class PlaylistViewModel @Inject constructor(
    playlistsRepository: PlaylistsRepository,
) : ViewModel() {

    val playlistsUiState: StateFlow<PlaylistsUiState> = playlistsRepository.getPlaylists()
        .map { playlists ->
            if (playlists.isEmpty()) {
                PlaylistsUiState.Empty
            } else {
                PlaylistsUiState.Success(
                    playlists = playlists,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = PlaylistsUiState.Loading,
        )
}

sealed interface PlaylistsUiState {

    data object Loading : PlaylistsUiState

    data object Empty : PlaylistsUiState

    data class Success(
        val playlists: List<Playlist>,
    ) : PlaylistsUiState
}
