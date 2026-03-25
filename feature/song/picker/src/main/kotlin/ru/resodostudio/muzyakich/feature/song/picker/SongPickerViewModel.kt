package ru.resodostudio.muzyakich.feature.song.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SongPickerViewModel @Inject constructor(
    songsRepository: SongsRepository,
) : ViewModel() {

    val songPickerUiState = songsRepository.getSongs(SortBy.ARTIST, SortOrder.ASCENDING)
        .map(SongPickerUiState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = SongPickerUiState.Loading,
        )
}

sealed interface SongPickerUiState {

    data object Loading : SongPickerUiState

    data object Error : SongPickerUiState

    data class Success(
        val songs: List<Song>,
    ) : SongPickerUiState
}