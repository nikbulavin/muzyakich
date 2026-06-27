package ru.resodostudio.muzyakich.ui.album.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.model.Album
import ru.resodostudio.muzyakich.core.model.Song
import ru.resodostudio.muzyakich.core.model.SortBy
import ru.resodostudio.muzyakich.core.model.SortOrder
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    songsRepository: SongsRepository,
) : ViewModel() {

    val albumsUiState = songsRepository.getSongs(SortBy.TITLE, SortOrder.ASCENDING)
        .map { songs ->
            if (songs.isEmpty()) {
                AlbumsUiState.Empty
            } else {
                AlbumsUiState.Success(
                    albums = songs.groupBy(Song::albumId).map { (albumId, songs) ->
                        Album(
                            id = albumId,
                            title = songs.firstOrNull()?.album ?: "<unknown>",
                            artist = songs.firstOrNull()?.artist ?: "<unknown>",
                            songs = songs,
                            year = null,
                            genre = null,
                        )
                    }
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = AlbumsUiState.Loading,
        )
}

sealed interface AlbumsUiState {

    data object Loading : AlbumsUiState

    data object Empty : AlbumsUiState

    data class Success(
        val albums: List<Album>,
    ) : AlbumsUiState
}
