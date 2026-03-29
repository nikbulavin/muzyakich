package ru.resodostudio.muzyakich.core.media.service

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.Player.EVENT_MEDIA_ITEM_TRANSITION
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_POSITION_MS
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.Main
import ru.resodostudio.muzyakich.core.media.service.mapper.asMediaItem
import ru.resodostudio.muzyakich.core.media.service.mapper.asQueueSong
import ru.resodostudio.muzyakich.core.media.service.util.asPlaybackState
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.core.model.data.QueueSong
import ru.resodostudio.muzyakich.core.model.data.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicServiceConnection @Inject constructor(
    @ApplicationContext context: Context,
    @Dispatcher(Main) mainDispatcher: CoroutineDispatcher,
) {
    private var mediaController: MediaController? = null
    private val coroutineScope = CoroutineScope(mainDispatcher + SupervisorJob())

    private val _nowPlayingState = MutableStateFlow(NowPlayingState())
    val nowPlayingState = _nowPlayingState.asStateFlow()

    private val _audioSessionId = MutableStateFlow<Int?>(null)
    val audioSessionId = _audioSessionId.asStateFlow()

    init {
        coroutineScope.launch {
            mediaController = MediaController.Builder(
                context,
                SessionToken(context, ComponentName(context, MusicService::class.java)),
            ).buildAsync().await().apply { addListener(PlayerListener()) }
        }
    }

    fun updateAudioSessionId(id: Int?) {
        _audioSessionId.update { id }
    }

    fun skipToSong(uid: String, position: Long = C.TIME_UNSET) {
        mediaController?.let { controller ->
            val timeline = controller.currentTimeline
            if (timeline.isEmpty) return

            val window = Timeline.Window()

            for (i in 0 until timeline.windowCount) {
                timeline.getWindow(i, window)
                if (window.uid.toString() == uid) {
                    controller.seekTo(i, position)
                    if (_nowPlayingState.value.playWhenReady) controller.play()
                    break
                }
            }
        }
    }

    fun playSongs(
        songs: List<Song>,
        startIndex: Int = DEFAULT_INDEX,
        startPositionMs: Long = DEFAULT_POSITION_MS,
        shuffle: Boolean = false,
    ) {
        mediaController?.let { controller ->
            controller.shuffleModeEnabled = shuffle
            val targetIndex = if (shuffle && songs.isNotEmpty()) {
                songs.indices.random()
            } else {
                startIndex
            }
            controller.setMediaItems(songs.map(Song::asMediaItem), targetIndex, startPositionMs)
            controller.prepare()
            controller.play()
        }
    }

    fun playSongsNext(songs: List<Song>) {
        mediaController?.let { controller ->
            val mediaItems = songs.map(Song::asMediaItem)
            if (controller.shuffleModeEnabled && !controller.currentTimeline.isEmpty) {
                val timeline = controller.currentTimeline
                val window = Timeline.Window()
                var newIndex = 0

                val fullQueue = buildList {
                    var index = timeline.getFirstWindowIndex(true)
                    while (index != C.INDEX_UNSET) {
                        add(timeline.getWindow(index, window).mediaItem)
                        if (index == controller.currentMediaItemIndex) {
                            newIndex = lastIndex
                            addAll(mediaItems)
                        }
                        index = timeline.getNextWindowIndex(index, Player.REPEAT_MODE_OFF, true)
                    }
                }

                controller.shuffleModeEnabled = false
                controller.setMediaItems(fullQueue, newIndex, controller.currentPosition)
                controller.prepare()
                controller.play()
            } else {
                controller.addMediaItems(controller.currentMediaItemIndex + 1, mediaItems)
            }
        }
    }

    fun moveSong(fromUid: String, toUid: String) {
        mediaController?.let { controller ->
            val timeline = controller.currentTimeline
            if (timeline.isEmpty) return

            var fromIndex = C.INDEX_UNSET
            var toIndex = C.INDEX_UNSET
            val window = Timeline.Window()

            for (i in 0 until timeline.windowCount) {
                timeline.getWindow(i, window)
                val itemUuid = window.uid.toString()
                if (itemUuid == fromUid) fromIndex = i
                if (itemUuid == toUid) toIndex = i
            }

            if (fromIndex != C.INDEX_UNSET && toIndex != C.INDEX_UNSET) {
                controller.moveMediaItem(fromIndex, toIndex)
            }
        }
    }

    fun removeSongFromQueue(uid: String) {
        mediaController?.let { controller ->
            val timeline = controller.currentTimeline
            if (timeline.isEmpty) return
            val window = Timeline.Window()
            for (index in timeline.windowCount - 1 downTo 0) {
                timeline.getWindow(index, window)
                if (window.uid.toString() == uid) {
                    controller.removeMediaItem(index)
                    break
                }
            }
        }
    }

    fun removeSongs(mediaIds: List<String>) {
        mediaController?.let { controller ->
            for (index in controller.mediaItemCount - 1 downTo 0) {
                if (controller.getMediaItemAt(index).mediaId in mediaIds) {
                    controller.removeMediaItem(index)
                }
            }
        }
    }

    private inner class PlayerListener : Player.Listener {

        override fun onEvents(player: Player, events: Player.Events) {
            if (events.containsAny(
                    EVENT_PLAYBACK_STATE_CHANGED,
                    EVENT_MEDIA_METADATA_CHANGED,
                    EVENT_PLAY_WHEN_READY_CHANGED,
                    EVENT_REPEAT_MODE_CHANGED,
                    EVENT_SHUFFLE_MODE_ENABLED_CHANGED,
                    EVENT_TIMELINE_CHANGED,
                    EVENT_MEDIA_ITEM_TRANSITION,
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
                playingQueue = getCurrentPlayingQueue(this),
                player = this,
            )
        }
    }

    private fun getCurrentPlayingQueue(player: Player): List<QueueSong> {
        val timeline = player.currentTimeline
        if (timeline.isEmpty) return emptyList()

        val result = mutableListOf<QueueSong>()
        val window = Timeline.Window()
        val currentIndex = player.currentMediaItemIndex
        if (currentIndex == C.INDEX_UNSET) return emptyList()

        var foundCurrent = false
        var windowIndex = timeline.getFirstWindowIndex(player.shuffleModeEnabled)

        while (windowIndex != C.INDEX_UNSET) {
            if (foundCurrent) {
                timeline.getWindow(windowIndex, window)
                result.add(window.mediaItem.asQueueSong(window.uid.toString()))
            } else if (windowIndex == currentIndex) {
                foundCurrent = true
            }

            windowIndex = timeline.getNextWindowIndex(
                windowIndex,
                Player.REPEAT_MODE_OFF,
                player.shuffleModeEnabled,
            )
        }
        return result
    }
}