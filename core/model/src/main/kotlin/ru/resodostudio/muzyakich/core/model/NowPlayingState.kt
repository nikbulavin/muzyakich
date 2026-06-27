package ru.resodostudio.muzyakich.core.model

import androidx.media3.common.Player
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_MEDIA_ID

data class NowPlayingState(
    val mediaId: String = DEFAULT_MEDIA_ID,
    val songIndex: Int = DEFAULT_INDEX,
    val playbackState: PlaybackState = PlaybackState.IDLE,
    val playWhenReady: Boolean = false,
    val playingQueue: List<QueueSong> = emptyList(),
    val player: Player? = null,
)