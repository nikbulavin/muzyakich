package ru.resodostudio.muzyakich.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.data.repository.MediaRepository
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.ui.util.convertToPosition
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    mediaRepository: MediaRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    val playerUiState = combine(
        musicServiceConnection.nowPlayingState,
        musicServiceConnection.currentPosition,
        mediaRepository.songs,
    ) { nowPlayingState, currentPosition, songs ->
        val currentSong = songs.find { it.mediaId == nowPlayingState.mediaId }

        if (currentSong == null) {
            PlayerUiState.Error
        } else {
            PlayerUiState.Success(
                nowPlayingState = nowPlayingState,
                currentPosition = currentPosition,
                currentSong = currentSong,
                songs = songs,
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PlayerUiState.Loading,
        )

    fun seekTo(position: Float) {
        musicServiceConnection.seekTo(
            convertToPosition(
                position,
                musicServiceConnection.nowPlayingState.value.duration,
            )
        )
    }

    fun play() = musicServiceConnection.play()
}

sealed interface PlayerUiState {

    data object Loading : PlayerUiState

    data object Error : PlayerUiState

    data class Success(
        val nowPlayingState: NowPlayingState,
        val currentPosition: Long,
        val currentSong: Song,
        val songs: List<Song>,
    ) : PlayerUiState
}