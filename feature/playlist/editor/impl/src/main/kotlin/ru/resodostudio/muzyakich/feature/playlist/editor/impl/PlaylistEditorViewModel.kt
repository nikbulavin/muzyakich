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
    private val _title = MutableStateFlow("")
    private val _coverFilePath = MutableStateFlow<String?>(null)
    private val _selectedCoverUri = MutableStateFlow<Uri?>(null)
    private val _songs = MutableStateFlow<List<Song>>(emptyList())

    val playlistEditorUiState = combine(
        _title,
        _coverFilePath,
        _songs,
        _selectedCoverUri,
    ) { name, coverFilePath, songs, selectedCoverUri ->
        PlaylistEditorUiState.Success(
            title = name,
            coverFilePath = coverFilePath,
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
                    _title.value = playlist.title
                    _coverFilePath.value = playlist.coverFilePath
                    _songs.value = playlist.songs
                }
            }
        }
    }

    fun onTitleChange(title: String) {
        _title.value = title
    }

    fun updateCover(uri: Uri?) {
        if (uri == null) return
        _selectedCoverUri.value = uri
    }

    fun removeCover() {
        _coverFilePath.value = null
        _selectedCoverUri.value = null
    }

    fun savePlaylist() {
        val name = _title.value
        if (name.isBlank()) return

        viewModelScope.launch {
            val existing = _playlist.value
            val playlist = Playlist(
                uuid = playlistUuid ?: Uuid.random(),
                title = name,
                timestamp = existing?.timestamp ?: Clock.System.now(),
                coverFilePath = _selectedCoverUri.value?.toString() ?: _coverFilePath.value,
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
        val title: String,
        val coverFilePath: String?,
        val songs: List<Song>,
        val selectedCoverUri: Uri? = null,
    ) : PlaylistEditorUiState
}