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
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import javax.inject.Inject
import kotlin.uuid.Uuid

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val songsRepository: SongsRepository,
    userDataRepository: UserDataRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    val playerUiState = combine(
        musicServiceConnection.nowPlayingState,
        songsRepository.getSongs(sortBy = SortBy.TITLE, sortOrder = SortOrder.ASCENDING),
        userDataRepository.userData,
    ) { nowPlayingState, songs, userData ->
        val currentSong = songs.find { it.mediaId == nowPlayingState.mediaId }

        if (currentSong == null) {
            PlayerUiState.Error
        } else {
            PlayerUiState.Success(
                nowPlayingState = nowPlayingState,
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

    fun skipToSong(uuid: Uuid) = musicServiceConnection.skipToSong(uuid)

    fun setSongFavorite(mediaId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            songsRepository.toggleFavorite(mediaId, isFavorite)
        }
    }
}

sealed interface PlayerUiState {

    data object Loading : PlayerUiState

    data object Error : PlayerUiState

    data class Success(
        val nowPlayingState: NowPlayingState,
        val currentSong: Song,
        val songs: List<Song>,
        val playbackConfig: PlaybackConfig,
    ) : PlayerUiState
}