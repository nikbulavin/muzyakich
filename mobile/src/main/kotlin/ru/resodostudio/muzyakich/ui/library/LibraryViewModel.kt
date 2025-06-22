package ru.resodostudio.muzyakich.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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

    private val shouldFilterFavoritesState = MutableStateFlow(false)

    val libraryUiState = combine(
        musicServiceConnection.nowPlayingState,
        musicServiceConnection.currentPosition,
        mediaRepository.songs,
        shouldFilterFavoritesState,
    ) { nowPlayingState, currentPosition, songs, shouldFilterFavorites ->
        if (songs.isEmpty()) {
            LibraryUiState.Empty
        } else {
            val currentSong = songs.find { it.mediaId == nowPlayingState.mediaId }
            val filteredSongs = songs
                .run {
                    if (shouldFilterFavorites) filter { it.isFavorite } else this
                }

            LibraryUiState.Success(
                nowPlayingState = nowPlayingState,
                currentPosition = currentPosition,
                currentSong = currentSong,
                songs = filteredSongs,
                shouldFilterFavorites = shouldFilterFavorites,
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LibraryUiState.Loading,
        )

    fun playSongs(songs: List<Song>, startIndex: Int = DEFAULT_INDEX) {
        musicServiceConnection.playSongs(songs = songs, startIndex = startIndex)
    }

    fun shuffleSongs(songs: List<Song>, startIndex: Int = DEFAULT_INDEX) {
        musicServiceConnection.shuffleSongs(songs = songs, startIndex = startIndex)
    }

    fun play() = musicServiceConnection.play()

    fun pause() = musicServiceConnection.pause()

    fun skipNext() = musicServiceConnection.skipToNext()

    fun toggleFilterFavorites() {
        shouldFilterFavoritesState.value = !shouldFilterFavoritesState.value
    }
}

sealed interface LibraryUiState {

    data object Loading : LibraryUiState

    data object Empty : LibraryUiState

    data class Success(
        val nowPlayingState: NowPlayingState,
        val currentPosition: Long,
        val currentSong: Song?,
        val songs: List<Song>,
        val shouldFilterFavorites: Boolean,
    ) : LibraryUiState
}