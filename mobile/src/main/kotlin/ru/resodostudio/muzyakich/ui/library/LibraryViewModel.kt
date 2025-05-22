package ru.resodostudio.muzyakich.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.data.repository.MediaRepository
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.data.Song
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    mediaRepository: MediaRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    val libraryUiState = mediaRepository.songs
        .map {
            if (it.isEmpty()) {
                LibraryUiState.Empty
            } else {
                LibraryUiState.Success(it)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LibraryUiState.Loading,
        )

    fun play(songs: List<Song>, startIndex: Int = DEFAULT_INDEX) =
        musicServiceConnection.playSongs(songs = songs, startIndex = startIndex)

}

sealed interface LibraryUiState {

    object Loading : LibraryUiState

    object Empty : LibraryUiState

    data class Success(
        val songs: List<Song>,
    ) : LibraryUiState
}