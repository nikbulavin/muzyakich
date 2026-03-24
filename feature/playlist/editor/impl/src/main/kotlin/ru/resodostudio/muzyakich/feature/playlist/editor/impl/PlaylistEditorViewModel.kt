package ru.resodostudio.muzyakich.feature.playlist.editor.impl

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.data.repository.PlaylistsRepository
import ru.resodostudio.muzyakich.core.model.data.Playlist
import ru.resodostudio.muzyakich.core.model.data.Song
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid

@HiltViewModel(assistedFactory = PlaylistEditorViewModel.Factory::class)
internal class PlaylistEditorViewModel @AssistedInject constructor(
    @Assisted val playlistUuid: Uuid?,
    private val playlistsRepository: PlaylistsRepository,
) : ViewModel() {

    private val _playlist = MutableStateFlow<Playlist?>(null)
    private val _name = MutableStateFlow("")
    private val _coverFileName = MutableStateFlow<String?>(null)
    private val _selectedCoverUri = MutableStateFlow<Uri?>(null)
    private val _songs = MutableStateFlow<List<Song>>(emptyList())

    val playlistEditorUiState = combine(
        _name,
        _coverFileName,
        _songs,
        _selectedCoverUri,
    ) { name, coverFileName, songs, selectedCoverUri ->
        PlaylistEditorUiState.Success(
            name = name,
            coverFileName = coverFileName,
            songs = songs,
            selectedCoverUri = selectedCoverUri,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = PlaylistEditorUiState.Loading,
        )

    init {
        if (playlistUuid != null) {
            viewModelScope.launch {
                val playlist = playlistsRepository.getPlaylist(playlistUuid).firstOrNull()
                if (playlist != null) {
                    _playlist.value = playlist
                    _name.value = playlist.name
                    _coverFileName.value = playlist.coverFileName
                    _songs.value = playlist.songs
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun updateCover(uri: Uri?) {
        if (uri == null) return
        _selectedCoverUri.value = uri
    }

    fun removeCover() {
        _coverFileName.value = null
        _selectedCoverUri.value = null
    }

    fun savePlaylist() {
        val name = _name.value
        if (name.isBlank()) return

        viewModelScope.launch {
            val existing = _playlist.value
            val playlist = Playlist(
                uuid = playlistUuid ?: Uuid.random(),
                name = name,
                timestamp = existing?.timestamp ?: Clock.System.now(),
                coverFileName = _coverFileName.value,
                songs = _songs.value,
            )
            playlistsRepository.upsertPlaylist(playlist)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            playlistUuid: Uuid?,
        ): PlaylistEditorViewModel
    }
}

sealed interface PlaylistEditorUiState {

    data object Loading : PlaylistEditorUiState

    data class Success(
        val name: String,
        val coverFileName: String?,
        val songs: List<Song>,
        val selectedCoverUri: Uri? = null,
    ) : PlaylistEditorUiState
}
