package ru.resodostudio.muzyakich.feature.player.impl

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.domain.CheckAndShowReviewUseCase
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.QueueSong
import ru.resodostudio.muzyakich.core.model.Song
import ru.resodostudio.muzyakich.core.model.SortBy
import ru.resodostudio.muzyakich.core.model.SortOrder
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class PlayerViewModel @Inject constructor(
    private val songsRepository: SongsRepository,
    private val musicServiceConnection: MusicServiceConnection,
    private val checkAndShowReviewUseCase: CheckAndShowReviewUseCase,
) : ViewModel() {

    val player = musicServiceConnection.playerState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = null,
        )

    val playerUiState = combine(
        musicServiceConnection.nowPlayingState,
        songsRepository.getSongs(sortBy = SortBy.TITLE, sortOrder = SortOrder.ASCENDING),
    ) { nowPlayingState, songs ->
        val currentSong = songs.find { it.mediaId == nowPlayingState.mediaId }

        if (currentSong == null) {
            PlayerUiState.Error
        } else {
            PlayerUiState.Success(
                playingQueue = nowPlayingState.playingQueue,
                currentSong = currentSong,
                songs = songs,
                isPlaying = nowPlayingState.isPlaying,
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

    fun requestReview(activity: Activity) {
        viewModelScope.launch {
            checkAndShowReviewUseCase(activity)
        }
    }
}

sealed interface PlayerUiState {

    data object Loading : PlayerUiState

    data object Error : PlayerUiState

    data class Success(
        val playingQueue: List<QueueSong>,
        val currentSong: Song,
        val songs: List<Song>,
        val isPlaying: Boolean,
    ) : PlayerUiState
}
