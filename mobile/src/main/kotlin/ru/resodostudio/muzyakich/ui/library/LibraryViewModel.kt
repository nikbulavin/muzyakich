package ru.resodostudio.muzyakich.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.data.repository.UserDataRepository
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.data.Artist
import ru.resodostudio.muzyakich.core.model.data.FilterConfig
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    songsRepository: SongsRepository,
    private val musicServiceConnection: MusicServiceConnection,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    private val shouldFilterFavoritesState = MutableStateFlow(false)

    val libraryUiState = userDataRepository.userData.flatMapLatest { userData ->
        combine(
            musicServiceConnection.nowPlayingState,
            musicServiceConnection.currentPosition,
            songsRepository.getSongs(userData.filterConfig.sortBy, userData.filterConfig.sortOrder),
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
                val artists = songs.groupBy(Song::artistId).map { (artistId, songs) ->
                    Artist(
                        id = artistId,
                        name = songs.firstOrNull()?.artist ?: "<unknown>",
                        songs = songs,
                    )
                }

                LibraryUiState.Success(
                    nowPlayingState = nowPlayingState,
                    currentPosition = currentPosition,
                    currentSong = currentSong,
                    songs = filteredSongs,
                    shouldFilterFavorites = shouldFilterFavorites,
                    artists = artists,
                    filterConfig = userData.filterConfig,
                )
            }
        }
    }
        .catch { LibraryUiState.Empty }
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

    fun playSongNext(song: Song) {
        musicServiceConnection.playSongNext(song)
    }

    fun updateSortByPreference(sortBy: SortBy) {
        viewModelScope.launch {
            userDataRepository.setSortByPreference(sortBy)
        }
    }

    fun updateSortOrderPreference(sortOrder: SortOrder) {
        viewModelScope.launch {
            userDataRepository.setSortOrderPreference(sortOrder)
        }
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
        val artists: List<Artist>,
        val filterConfig: FilterConfig,
    ) : LibraryUiState
}