package ru.resodostudio.muzyakich.feature.player.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class PlayerViewModel @Inject constructor(
    private val songsRepository: SongsRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    val playerUiState = combine(
        musicServiceConnection.nowPlayingState,
        songsRepository.getSongs(sortBy = SortBy.TITLE, sortOrder = SortOrder.ASCENDING),
    ) { nowPlayingState, songs ->
        val currentSong = songs.find { it.mediaId == nowPlayingState.mediaId }

        if (currentSong == null) {
            PlayerUiState.Error
        } else {
            PlayerUiState.Success(
                nowPlayingState = nowPlayingState,
                currentSong = currentSong,
                songs = songs,
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = PlayerUiState.Loading,
        )

    fun skipToSong(uid: String) = musicServiceConnection.skipToSong(uid)

    fun removeSong(uid: String) = musicServiceConnection.removeSongFromQueue(uid)
    
    fun moveSong(fromUid: String, toUid: String) = musicServiceConnection.moveSong(fromUid, toUid)

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
    ) : PlayerUiState
}
