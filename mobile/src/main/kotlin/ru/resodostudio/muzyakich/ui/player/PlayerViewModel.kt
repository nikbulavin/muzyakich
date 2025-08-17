package ru.resodostudio.muzyakich.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.data.repository.UserDataRepository
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.core.model.data.PlaybackConfig
import ru.resodostudio.muzyakich.core.model.data.RepeatMode
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.ui.util.convertToPosition
import javax.inject.Inject
import kotlin.uuid.Uuid

@HiltViewModel
class PlayerViewModel @Inject constructor(
    songsRepository: SongsRepository,
    private val userDataRepository: UserDataRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    val playerUiState = combine(
        musicServiceConnection.nowPlayingState,
        musicServiceConnection.currentPosition,
        songsRepository.getSongs(),
        userDataRepository.userData,
    ) { nowPlayingState, currentPosition, songs, userData ->
        val currentSong = songs.find { it.mediaId == nowPlayingState.mediaId }

        if (currentSong == null) {
            PlayerUiState.Error
        } else {
            PlayerUiState.Success(
                nowPlayingState = nowPlayingState,
                currentPosition = currentPosition,
                currentSong = currentSong,
                songs = songs,
                playbackConfig = userData.playbackConfig,
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

    fun pause() = musicServiceConnection.pause()

    fun skipToNext() = musicServiceConnection.skipToNext()

    fun skipToSong(uuid: Uuid) = musicServiceConnection.skipToSong(uuid)

    fun skipToPrevious() = musicServiceConnection.skipToPrevious()

    fun setShuffleModeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            userDataRepository.setShuffleModePreference(enabled)
        }
    }

    fun setRepeatMode(repeatMode: RepeatMode) {
        viewModelScope.launch {
            userDataRepository.setRepeatModePreference(repeatMode)
        }
    }
}

sealed interface PlayerUiState {

    data object Loading : PlayerUiState

    data object Error : PlayerUiState

    data class Success(
        val nowPlayingState: NowPlayingState,
        val currentPosition: Long,
        val currentSong: Song,
        val songs: List<Song>,
        val playbackConfig: PlaybackConfig,
    ) : PlayerUiState
}