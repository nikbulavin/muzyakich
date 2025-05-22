package ru.resodostudio.muzyakich.core.media.service

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.common.Player.EVENT_MEDIA_ITEM_TRANSITION
import androidx.media3.common.Player.EVENT_MEDIA_METADATA_CHANGED
import androidx.media3.common.Player.EVENT_PLAYBACK_STATE_CHANGED
import androidx.media3.common.Player.EVENT_PLAY_WHEN_READY_CHANGED
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.maximillianleonov.musicmax.core.domain.usecase.GetPlayingQueueIdsUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetPlayingQueueIndexUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.SetPlayingQueueIdsUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.SetPlayingQueueIndexUseCase
import com.maximillianleonov.musicmax.core.model.MusicState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_POSITION_MS
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.Main
import ru.resodostudio.muzyakich.core.data.repository.MediaRepository
import ru.resodostudio.muzyakich.core.media.service.mapper.asMediaItem
import ru.resodostudio.muzyakich.core.media.service.util.asPlaybackState
import ru.resodostudio.muzyakich.core.media.service.util.orDefaultTimestamp
import ru.resodostudio.muzyakich.core.model.data.Song
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class MusicServiceConnection @Inject constructor(
    @ApplicationContext context: Context,
    @Dispatcher(Main) mainDispatcher: CoroutineDispatcher,
    private val mediaRepository: MediaRepository,
    private val getPlayingQueueIdsUseCase: GetPlayingQueueIdsUseCase,
    private val setPlayingQueueIdsUseCase: SetPlayingQueueIdsUseCase,
    private val getPlayingQueueIndexUseCase: GetPlayingQueueIndexUseCase,
    private val setPlayingQueueIndexUseCase: SetPlayingQueueIndexUseCase
) {
    private var mediaController: MediaController? = null
    private val coroutineScope = CoroutineScope(mainDispatcher + SupervisorJob())

    private val _musicState = MutableStateFlow(MusicState())
    val musicState = _musicState.asStateFlow()

    val currentPosition = flow {
        while (currentCoroutineContext().isActive) {
            val currentPosition = mediaController?.currentPosition ?: DEFAULT_POSITION_MS
            emit(currentPosition)
            delay(1.milliseconds)
        }
    }

    init {
        coroutineScope.launch {
            mediaController = MediaController.Builder(
                context,
                SessionToken(context, ComponentName(context, MusicService::class.java))
            ).buildAsync().await().apply { addListener(PlayerListener()) }
            updatePlayingQueue()
        }
    }

    fun skipPrevious() = mediaController?.run {
        seekToPrevious()
        play()
    }

    fun play() = mediaController?.play()
    fun pause() = mediaController?.pause()

    fun skipNext() = mediaController?.run {
        seekToNext()
        play()
    }

    fun skipTo(position: Long) = mediaController?.run {
        seekTo(position)
        play()
    }

    fun skipToIndex(index: Int, position: Long = DEFAULT_POSITION_MS) = mediaController?.run {
        seekTo(index, position)
        play()
    }

    fun playSongs(
        songs: List<Song>,
        startIndex: Int = DEFAULT_INDEX,
        startPositionMs: Long = DEFAULT_POSITION_MS
    ) {
        mediaController?.run {
            setMediaItems(songs.map(Song::asMediaItem), startIndex, startPositionMs)
            prepare()
            play()
        }
        coroutineScope.launch { setPlayingQueueIdsUseCase(playingQueueIds = songs.map(Song::mediaId)) }
    }

    fun shuffleSongs(
        songs: List<Song>,
        startIndex: Int = DEFAULT_INDEX,
        startPositionMs: Long = DEFAULT_POSITION_MS
    ) = playSongs(
        songs = songs.shuffled(),
        startIndex = startIndex,
        startPositionMs = startPositionMs
    )

    private inner class PlayerListener : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            if (events.containsAny(
                    EVENT_PLAYBACK_STATE_CHANGED,
                    EVENT_MEDIA_METADATA_CHANGED,
                    EVENT_PLAY_WHEN_READY_CHANGED
                )
            ) {
                updateMusicState(player)
            }

            if (events.contains(EVENT_MEDIA_ITEM_TRANSITION)) {
                updatePlayingQueueIndex(player)
            }
        }
    }

    private fun updateMusicState(player: Player) = with(player) {
        _musicState.update {
            it.copy(
                currentMediaId = currentMediaItem?.mediaId.orEmpty(),
                playbackState = playbackState.asPlaybackState(),
                playWhenReady = playWhenReady,
                duration = duration.orDefaultTimestamp()
            )
        }
    }

    private suspend fun updatePlayingQueue(startPositionMs: Long = DEFAULT_POSITION_MS) {
        val songs = mediaRepository.songs.first()
        if (songs.isEmpty()) return

        val playingQueueSongs = getPlayingQueueIdsUseCase().first().mapNotNull { playingQueueId ->
            songs.find { it.mediaId == playingQueueId }
        }.ifEmpty {
            setPlayingQueueIdsUseCase(playingQueueIds = songs.map(Song::mediaId))
            songs
        }

        val startIndex = getPlayingQueueIndexUseCase().first().let { startIndex ->
            if (startIndex < playingQueueSongs.size) {
                startIndex
            } else {
                setPlayingQueueIndexUseCase(index = 0)
                0
            }
        }

        mediaController?.run {
            setMediaItems(playingQueueSongs.map(Song::asMediaItem), startIndex, startPositionMs)
            prepare()
        }
    }

    private fun updatePlayingQueueIndex(player: Player) {
        val index = player.currentMediaItemIndex
        _musicState.update { it.copy(currentSongIndex = index) }
        coroutineScope.launch { setPlayingQueueIndexUseCase(index) }
    }
}
