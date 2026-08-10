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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.data.repository.PlaylistsRepository
import ru.resodostudio.muzyakich.core.model.Playlist
import ru.resodostudio.muzyakich.core.model.PlaylistSong
import ru.resodostudio.muzyakich.core.model.Song
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid

@HiltViewModel(assistedFactory = PlaylistEditorViewModel.Factory::class)
internal class PlaylistEditorViewModel @AssistedInject constructor(
    @Assisted val playlistUuid: Uuid?,
    private val playlistsRepository: PlaylistsRepository,
) : ViewModel() {

    private val playlistState = MutableStateFlow<Playlist?>(null)
    private val titleState = MutableStateFlow("")
    private val coverFilePathState = MutableStateFlow<String?>(null)
    private val selectedCoverUriState = MutableStateFlow<Uri?>(null)
    private val songsState = MutableStateFlow<List<PlaylistSong>>(emptyList())

    val playlistEditorUiState = combine(
        titleState,
        coverFilePathState,
        songsState,
        selectedCoverUriState,
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
                    playlistState.value = playlist
                    titleState.value = playlist.title
                    coverFilePathState.value = playlist.coverFilePath
                    songsState.value = playlist.songs
                }
            }
        }
    }

    fun onTitleChange(title: String) {
        titleState.value = title
    }

    fun updateCover(uri: Uri?) {
        if (uri == null) return
        selectedCoverUriState.value = uri
    }

    fun removeCover() {
        coverFilePathState.value = null
        selectedCoverUriState.value = null
    }

    fun addSongs(songs: List<Song>) {
        songsState.update { currentSongs ->
            currentSongs + songs.map { song -> PlaylistSong(uuid = Uuid.random(), song = song) }
        }
    }

    fun removeSong(playlistSongUuid: Uuid) {
        songsState.update { currentSongs ->
            currentSongs.filter { it.uuid != playlistSongUuid }
        }
    }

    fun reorderSongs(fromIndex: Int, toIndex: Int) {
        songsState.update { currentSongs ->
            currentSongs.toMutableList().apply {
                add(toIndex, removeAt(fromIndex))
            }
        }
    }

    fun savePlaylist() {
        val name = titleState.value
        if (name.isBlank()) return

        viewModelScope.launch {
            val existing = playlistState.value
            val playlist = Playlist(
                uuid = playlistUuid ?: Uuid.random(),
                title = name,
                timestamp = existing?.timestamp ?: Clock.System.now(),
                coverFilePath = selectedCoverUriState.value?.toString() ?: coverFilePathState.value,
                songs = songsState.value,
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
        val songs: List<PlaylistSong>,
        val selectedCoverUri: Uri? = null,
    ) : PlaylistEditorUiState
}
