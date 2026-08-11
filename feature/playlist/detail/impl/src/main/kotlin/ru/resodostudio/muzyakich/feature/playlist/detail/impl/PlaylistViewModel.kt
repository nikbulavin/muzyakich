package ru.resodostudio.muzyakich.feature.playlist.detail.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.data.repository.PlaylistsRepository
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.NowPlayingState
import ru.resodostudio.muzyakich.core.model.Playlist
import ru.resodostudio.muzyakich.core.model.PlaylistSong
import ru.resodostudio.muzyakich.core.model.Song
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid

@HiltViewModel(assistedFactory = PlaylistViewModel.Factory::class)
internal class PlaylistViewModel @AssistedInject constructor(
    @Assisted val playlistUuid: Uuid,
    private val playlistsRepository: PlaylistsRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    val playlistUiState: StateFlow<PlaylistUiState> = combine(
        playlistsRepository.getPlaylist(playlistUuid),
        musicServiceConnection.nowPlayingState,
    ) { playlist, nowPlaying ->
        if (playlist == null) {
            PlaylistUiState.Error
        } else {
            PlaylistUiState.Success(
                playlist = playlist,
                nowPlayingState = nowPlaying,
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = PlaylistUiState.Loading,
        )

    fun playSongs(
        songs: List<PlaylistSong>,
        startIndex: Int = DEFAULT_INDEX,
        shuffle: Boolean = false,
    ) {
        musicServiceConnection.playSongs(
            songs = songs.map { it.song },
            startIndex = startIndex,
            shuffle = shuffle,
        )
    }

    fun playSongsNext(songs: List<PlaylistSong>) {
        musicServiceConnection.playSongsNext(songs.map { it.song })
    }

    fun playSongNext(song: Song) {
        musicServiceConnection.playSongsNext(listOf(song))
    }

    fun removeSongFromPlaylist(playlistSongUuid: Uuid) {
        viewModelScope.launch {
            playlistsRepository.removeSongFromPlaylist(playlistSongUuid)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistsRepository.deletePlaylist(playlistUuid)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            playlistUuid: Uuid,
        ): PlaylistViewModel
    }
}

sealed interface PlaylistUiState {

    data object Loading : PlaylistUiState

    data object Error : PlaylistUiState

    data class Success(
        val playlist: Playlist,
        val nowPlayingState: NowPlayingState,
    ) : PlaylistUiState
}
