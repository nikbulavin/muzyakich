package ru.resodostudio.muzyakich.ui.song.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.data.repository.UserDataRepository
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.data.FilterConfig
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SongsViewModel @Inject constructor(
    private val songsRepository: SongsRepository,
    private val musicServiceConnection: MusicServiceConnection,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    val songsUiState = userDataRepository.userData.flatMapLatest { userData ->
        combine(
            musicServiceConnection.nowPlayingState,
            songsRepository.getSongs(userData.filterConfig.sortBy, userData.filterConfig.sortOrder),
        ) { nowPlayingState, songs ->
            if (songs.isEmpty()) {
                SongsUiState.Empty
            } else {
                val filteredSongs = if (userData.filterConfig.shouldFilterFavorites) {
                    songs.filter { it.isFavorite }
                } else {
                    songs
                }

                SongsUiState.Success(
                    currentMediaId = nowPlayingState.player?.currentMediaItem?.mediaId,
                    isPlaying = nowPlayingState.player?.isPlaying ?: false,
                    songs = filteredSongs,
                    filterConfig = userData.filterConfig,
                )
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = SongsUiState.Loading,
        )

    fun playSongs(songs: List<Song>, startIndex: Int = DEFAULT_INDEX, shuffle: Boolean = false) {
        musicServiceConnection.playSongs(songs = songs, startIndex = startIndex, shuffle = shuffle)
    }

    fun toggleFilterFavorites(shouldFilterFavorites: Boolean) {
        viewModelScope.launch {
            userDataRepository.setFilterFavoritesPreference(shouldFilterFavorites)
        }
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

    fun playSongNext(song: Song) {
        musicServiceConnection.playSongsNext(listOf(song))
    }
}

sealed interface SongsUiState {

    data object Loading : SongsUiState

    data object Empty : SongsUiState

    data class Success(
        val currentMediaId: String?,
        val isPlaying: Boolean,
        val songs: List<Song>,
        val filterConfig: FilterConfig,
    ) : SongsUiState
}