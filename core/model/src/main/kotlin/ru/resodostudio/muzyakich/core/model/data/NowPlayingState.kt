package ru.resodostudio.muzyakich.core.model.data

import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_DURATION_MS
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_MEDIA_ID
import ru.resodostudio.muzyakich.core.model.data.PlaybackState.IDLE

data class NowPlayingState(
    val mediaId: String = DEFAULT_MEDIA_ID,
    val songIndex: Int = DEFAULT_INDEX,
    val playbackState: PlaybackState = IDLE,
    val playWhenReady: Boolean = false,
    val duration: Long = DEFAULT_DURATION_MS,
)