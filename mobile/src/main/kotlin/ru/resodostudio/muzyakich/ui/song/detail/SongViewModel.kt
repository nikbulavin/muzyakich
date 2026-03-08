package ru.resodostudio.muzyakich.ui.song.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.data.Song
import kotlin.time.Duration.Companion.seconds

@HiltViewModel(assistedFactory = SongViewModel.Factory::class)
class SongViewModel @AssistedInject constructor(
    @Assisted val mediaId: String,
    private val musicServiceConnection: MusicServiceConnection,
    private val songsRepository: SongsRepository,
) : ViewModel() {

    val songUiState = songsRepository.getSong(mediaId)
        .map { song ->
            if (song == null) {
                SongUiState.Error
            } else {
                SongUiState.Success(
                    song = song,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = SongUiState.Loading,
        )

    fun playSongNext(song: Song) {
        musicServiceConnection.playSongNext(song)
    }

    fun setSongFavorite(mediaId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            songsRepository.toggleFavorite(mediaId, isFavorite)
        }
    }

    fun removeSong(mediaId: String) {
        musicServiceConnection.removeSong(mediaId)
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
    ) : SongUiState
}