package ru.resodostudio.muzyakich.core.media.service.util

import androidx.media3.common.C
import androidx.media3.common.Player
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_DURATION_MS
import ru.resodostudio.muzyakich.core.model.data.PlaybackState

internal fun Int.asPlaybackState() = when (this) {
    Player.STATE_IDLE -> PlaybackState.IDLE
    Player.STATE_BUFFERING -> PlaybackState.BUFFERING
    Player.STATE_READY -> PlaybackState.READY
    Player.STATE_ENDED -> PlaybackState.ENDED
    else -> error("Invalid playback state.")
}

internal fun Long.orDefaultTimestamp() = takeIf { it != C.TIME_UNSET } ?: DEFAULT_DURATION_MS
