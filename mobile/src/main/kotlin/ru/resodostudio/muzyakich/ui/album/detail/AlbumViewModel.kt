package ru.resodostudio.muzyakich.ui.album.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.Default
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.data.Album
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import kotlin.time.Duration.Companion.seconds

@HiltViewModel(assistedFactory = AlbumViewModel.Factory::class)
class AlbumViewModel @AssistedInject constructor(
    @Assisted val albumId: Long,
    songsRepository: SongsRepository,
    private val musicServiceConnection: MusicServiceConnection,
    @Dispatcher(Default) private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val albumUiState = combine(
        songsRepository.getSongs(SortBy.TITLE, SortOrder.ASCENDING),
        musicServiceConnection.nowPlayingState,
    ) { songs, nowPlaying ->
        val albumSongs = songs.filter { it.albumId == albumId }
        val firstYear = albumSongs.firstOrNull()?.year ?: 0
        val year = if (firstYear != 0 && albumSongs.all { it.year == firstYear }) firstYear else null
        val album = Album(
            id = albumId,
            title = albumSongs.firstOrNull()?.album ?: "<unknown>",
            artist = albumSongs.firstOrNull()?.artist ?: "<unknown>",
            year = year,
            songs = albumSongs.sortedBy { it.trackNumber },
        )
        if (albumSongs.isEmpty()) {
            AlbumUiState.Error
        } else {
            AlbumUiState.Success(
                album = album,
                nowPlayingState = nowPlaying,
            )
        }
    }
        .catch { AlbumUiState.Error }
        .flowOn(defaultDispatcher)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = AlbumUiState.Loading,
        )

    fun playSongs(songs: List<Song>, startIndex: Int = DEFAULT_INDEX, shuffle: Boolean = false) {
        musicServiceConnection.playSongs(songs = songs, startIndex = startIndex, shuffle = shuffle)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            albumId: Long,
        ): AlbumViewModel
    }
}

sealed interface AlbumUiState {

    data object Loading : AlbumUiState

    data object Error : AlbumUiState

    data class Success(
        val album: Album,
        val nowPlayingState: NowPlayingState,
    ) : AlbumUiState
}
