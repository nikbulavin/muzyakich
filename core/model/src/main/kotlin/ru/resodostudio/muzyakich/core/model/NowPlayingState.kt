package ru.resodostudio.muzyakich.core.model

import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_MEDIA_ID

data class NowPlayingState(
    val mediaId: String = DEFAULT_MEDIA_ID,
    val songIndex: Int = DEFAULT_INDEX,
    val isPlaying: Boolean = false,
    val playbackState: PlaybackState = PlaybackState.IDLE,
    val playWhenReady: Boolean = false,
    val playingQueue: List<QueueSong> = emptyList(),
)