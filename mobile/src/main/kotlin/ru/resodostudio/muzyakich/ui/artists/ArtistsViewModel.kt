package ru.resodostudio.muzyakich.ui.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.model.data.Artist
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ArtistsViewModel @Inject constructor(
    songsRepository: SongsRepository,
) : ViewModel() {

    val artistsUiState = songsRepository.getSongs(SortBy.ARTIST, SortOrder.ASCENDING)
        .map { songs ->
            if (songs.isEmpty()) {
                ArtistsUiState.Empty
            } else {
                ArtistsUiState.Success(
                    artists = songs.groupBy(Song::artistId).map { (artistId, songs) ->
                        Artist(
                            id = artistId,
                            name = songs.firstOrNull()?.artist ?: "<unknown>",
                            songs = songs,
                        )
                    }
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = ArtistsUiState.Loading,
        )
}

sealed interface ArtistsUiState {

    data object Loading : ArtistsUiState

    data object Empty : ArtistsUiState

    data class Success(
        val artists: List<Artist>,
    ) : ArtistsUiState
}