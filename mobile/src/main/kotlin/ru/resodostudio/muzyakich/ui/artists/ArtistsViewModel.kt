package ru.resodostudio.muzyakich.ui.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.domain.GetArtistsUseCase
import ru.resodostudio.muzyakich.core.model.data.Artist
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ArtistsViewModel @Inject constructor(
    getArtistsUseCase: GetArtistsUseCase,
) : ViewModel() {

    val artistsUiState = getArtistsUseCase.invoke(SortBy.ARTIST, SortOrder.ASCENDING)
        .map { artists ->
            if (artists.isEmpty()) {
                ArtistsUiState.Empty
            } else {
                ArtistsUiState.Success(
                    artists = artists,
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