package ru.resodostudio.muzyakich.feature.artist.detail.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.domain.GetArtistUseCase
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.Artist
import ru.resodostudio.muzyakich.core.model.NowPlayingState
import ru.resodostudio.muzyakich.core.model.Song
import kotlin.time.Duration.Companion.seconds

@HiltViewModel(assistedFactory = ArtistViewModel.Factory::class)
internal class ArtistViewModel @AssistedInject constructor(
    @Assisted val artistId: Long,
    getArtistUseCase: GetArtistUseCase,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    val artistUiState = combine(
        getArtistUseCase.invoke(artistId),
        musicServiceConnection.nowPlayingState,
    ) { artist, nowPlaying ->
        if (artist == null) {
            ArtistUiState.Error
        } else {
            ArtistUiState.Success(
                artist = artist,
                nowPlayingState = nowPlaying,
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = ArtistUiState.Loading,
        )

    fun playSongs(songs: List<Song>, startIndex: Int = DEFAULT_INDEX, shuffle: Boolean = false) {
        musicServiceConnection.playSongs(songs = songs, startIndex = startIndex, shuffle = shuffle)
    }

    fun playSongNext(song: Song) {
        musicServiceConnection.playSongsNext(listOf(song))
    }

    fun removeSong(mediaId: String) {
        musicServiceConnection.removeSongs(listOf(mediaId))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            artistId: Long,
        ): ArtistViewModel
    }
}

internal sealed interface ArtistUiState {

    data object Loading : ArtistUiState

    data object Error : ArtistUiState

    data class Success(
        val artist: Artist,
        val nowPlayingState: NowPlayingState,
    ) : ArtistUiState
}
