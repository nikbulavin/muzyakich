package ru.resodostudio.muzyakich.feature.song.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.model.Song
import ru.resodostudio.muzyakich.core.model.SortBy
import ru.resodostudio.muzyakich.core.model.SortOrder
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SongPickerViewModel @Inject constructor(
    songsRepository: SongsRepository,
) : ViewModel() {

    private val _selectedSongs = MutableStateFlow<Set<String>>(emptySet())

    val songPickerUiState = combine(
        songsRepository.getSongs(SortBy.ARTIST, SortOrder.ASCENDING),
        _selectedSongs,
    ) { songs, selectedSongs ->
        SongPickerUiState.Success(
            songs = songs,
            selectedSongs = selectedSongs,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = SongPickerUiState.Loading,
        )

    fun toggleSong(mediaId: String) {
        _selectedSongs.update { current ->
            if (mediaId in current) current - mediaId else current + mediaId
        }
    }

    fun clearSelectedSongs() {
        _selectedSongs.value = emptySet()
    }
}

sealed interface SongPickerUiState {

    data object Loading : SongPickerUiState

    data object Error : SongPickerUiState

    data class Success(
        val songs: List<Song>,
        val selectedSongs: Set<String>,
    ) : SongPickerUiState
}
