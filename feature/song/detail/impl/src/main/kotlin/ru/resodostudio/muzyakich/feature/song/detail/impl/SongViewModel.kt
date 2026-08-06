package ru.resodostudio.muzyakich.feature.song.detail.impl

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.data.repository.PlaylistsRepository
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.data.repository.util.ReviewManager
import ru.resodostudio.muzyakich.core.domain.CheckIfReviewNeededUseCase
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.Playlist
import ru.resodostudio.muzyakich.core.model.Song
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid

@HiltViewModel(assistedFactory = SongViewModel.Factory::class)
internal class SongViewModel @AssistedInject constructor(
    @Assisted val mediaId: String,
    private val musicServiceConnection: MusicServiceConnection,
    private val songsRepository: SongsRepository,
    private val playlistsRepository: PlaylistsRepository,
    private val checkIfReviewNeededUseCase: CheckIfReviewNeededUseCase,
    private val reviewManager: ReviewManager,
) : ViewModel() {

    private val shouldShowReviewDialogState = MutableStateFlow(false)

    val songUiState = combine(
        songsRepository.getSong(mediaId),
        playlistsRepository.getPlaylists(),
        shouldShowReviewDialogState,
    ) { song, playlists, shouldShowReviewDialog ->
        if (song == null) {
            SongUiState.Error
        } else {
            SongUiState.Success(
                song = song,
                playlists = playlists,
                shouldShowReviewDialog = shouldShowReviewDialog,
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = SongUiState.Loading,
        )

    fun playSongNext(song: Song) {
        musicServiceConnection.playSongsNext(listOf(song))
    }

    fun setSongFavorite(mediaId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            songsRepository.toggleFavorite(mediaId, isFavorite)
            if (checkIfReviewNeededUseCase()) shouldShowReviewDialogState.value = true
        }
    }

    fun removeSong(mediaId: String) {
        musicServiceConnection.removeSongs(listOf(mediaId))
    }

    fun addToPlaylist(playlistUuid: Uuid, mediaId: String, position: Int) {
        viewModelScope.launch {
            playlistsRepository.addSongToPlaylist(playlistUuid, mediaId, position)
        }
    }

    fun requestReviewDialog(activity: Activity) {
        viewModelScope.launch {
            reviewManager.requestReview(activity)
        }
        shouldShowReviewDialogState.value = false
    }

    @AssistedFactory
    interface Factory {
        fun create(
            mediaId: String,
        ): SongViewModel
    }
}

sealed interface SongUiState {

    data object Loading : SongUiState

    data object Error : SongUiState

    data class Success(
        val song: Song,
        val playlists: List<Playlist>,
        val shouldShowReviewDialog: Boolean,
    ) : SongUiState
}