package ru.resodostudio.muzyakich.core.media.service

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.Player.EVENT_MEDIA_METADATA_CHANGED
import androidx.media3.common.Player.EVENT_PLAYBACK_STATE_CHANGED
import androidx.media3.common.Player.EVENT_PLAY_WHEN_READY_CHANGED
import androidx.media3.common.Player.EVENT_REPEAT_MODE_CHANGED
import androidx.media3.common.Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED
import androidx.media3.common.Player.EVENT_TIMELINE_CHANGED
import androidx.media3.common.Timeline
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_POSITION_MS
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.Main
import ru.resodostudio.muzyakich.core.media.service.mapper.asMediaItem
import ru.resodostudio.muzyakich.core.media.service.mapper.asSong
import ru.resodostudio.muzyakich.core.media.service.util.asPlaybackState
import ru.resodostudio.muzyakich.core.media.service.util.orDefaultTimestamp
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.core.model.data.Song
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class MusicServiceConnection @Inject constructor(
    @ApplicationContext context: Context,
    @Dispatcher(Main) mainDispatcher: CoroutineDispatcher,
) {
    private var mediaController: MediaController? = null
    private val coroutineScope = CoroutineScope(mainDispatcher + SupervisorJob())

    private val _nowPlayingState = MutableStateFlow(NowPlayingState())
    val nowPlayingState = _nowPlayingState.asStateFlow()

    val currentPosition = flow {
        while (currentCoroutineContext().isActive) {
            val currentPosition = mediaController?.currentPosition ?: DEFAULT_POSITION_MS
            emit(currentPosition)
            delay(25.milliseconds)
        }
    }

    init {
        coroutineScope.launch {
            mediaController = MediaController.Builder(
                context,
                SessionToken(context, ComponentName(context, MusicService::class.java))
            ).buildAsync().await().apply { addListener(PlayerListener()) }
        }
    }

    fun skipToPrevious() = mediaController?.run {
        seekToPrevious()
        if (_nowPlayingState.value.playWhenReady) play()
    }

    fun play() = mediaController?.play()
    fun pause() = mediaController?.pause()

    fun skipToNext() = mediaController?.run {
        seekToNext()
        if (_nowPlayingState.value.playWhenReady) play()
    }

    fun seekTo(position: Long) = mediaController?.run {
        seekTo(position)
        if (_nowPlayingState.value.playWhenReady) play()
    }

    fun skipToIndex(index: Int, position: Long = DEFAULT_POSITION_MS) = mediaController?.run {
        seekTo(index, position)
        if (_nowPlayingState.value.playWhenReady) play()
    }

    fun playSongs(
        songs: List<Song>,
        startIndex: Int = DEFAULT_INDEX,
        startPositionMs: Long = DEFAULT_POSITION_MS,
    ) {
        mediaController?.run {
            setMediaItems(songs.map(Song::asMediaItem), startIndex, startPositionMs)
            prepare()
            play()
        }
    }

    fun shuffleSongs(
        songs: List<Song>,
        startIndex: Int = DEFAULT_INDEX,
        startPositionMs: Long = DEFAULT_POSITION_MS,
    ) = playSongs(
        songs = songs.shuffled(),
        startIndex = startIndex,
        startPositionMs = startPositionMs,
    )

    private inner class PlayerListener : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            if (events.containsAny(
                    EVENT_PLAYBACK_STATE_CHANGED,
                    EVENT_MEDIA_METADATA_CHANGED,
                    EVENT_PLAY_WHEN_READY_CHANGED,
                    EVENT_REPEAT_MODE_CHANGED,
                    EVENT_SHUFFLE_MODE_ENABLED_CHANGED,
                    EVENT_TIMELINE_CHANGED,
                )
            ) {
                updateNowPlayingState(player)
            }
        }
    }

    private fun updateNowPlayingState(player: Player) = with(player) {
        _nowPlayingState.update {
            it.copy(
                mediaId = currentMediaItem?.mediaId.orEmpty(),
                playbackState = playbackState.asPlaybackState(),
                playWhenReady = playWhenReady,
                duration = duration.orDefaultTimestamp(),
                hasNextMediaItem = hasNextMediaItem(),
                playingQueue = getCurrentPlayingQueue(this),
            )
        }
    }

    private fun getCurrentPlayingQueue(player: Player): List<Song> {
        val timeline = player.currentTimeline
        if (timeline.isEmpty) return emptyList()

        val result = mutableListOf<Song>()
        val window = Timeline.Window()
        val currentMediaItem = player.currentMediaItem ?: return emptyList()
        var foundCurrent = false

        var windowIndex = timeline.getFirstWindowIndex(player.shuffleModeEnabled)

        while (windowIndex != C.INDEX_UNSET) {
            timeline.getWindow(windowIndex, window)
            val mediaItem = window.mediaItem

            if (foundCurrent) {
                result.add(mediaItem.asSong())
            } else if (mediaItem.mediaId == currentMediaItem.mediaId) {
                foundCurrent = true
            }

            windowIndex = timeline.getNextWindowIndex(
                windowIndex,
                player.repeatMode,
                player.shuffleModeEnabled,
            )
        }

        return result
    }
}
