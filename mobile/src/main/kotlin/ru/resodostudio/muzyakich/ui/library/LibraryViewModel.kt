package ru.resodostudio.muzyakich.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.data.repository.MediaRepository
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.core.model.data.Song
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    mediaRepository: MediaRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    val libraryUiState = combine(
        musicServiceConnection.nowPlayingState,
        musicServiceConnection.currentPosition,
        mediaRepository.songs,
    ) { musicState, currentPosition, songs ->
        if (songs.isEmpty()) {
            LibraryUiState.Empty
        } else {
            LibraryUiState.Success(musicState, currentPosition, songs)
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LibraryUiState.Loading,
        )

    fun playSongs(songs: List<Song>, startIndex: Int = DEFAULT_INDEX) =
        musicServiceConnection.playSongs(songs = songs, startIndex = startIndex)

    fun play() = musicServiceConnection.play()

    fun pause() = musicServiceConnection.pause()

}

sealed interface LibraryUiState {

    data object Loading : LibraryUiState

    data object Empty : LibraryUiState

    data class Success(
        val nowPlayingState: NowPlayingState,
        val currentPosition: Long,
        val songs: List<Song>,
    ) : LibraryUiState
}