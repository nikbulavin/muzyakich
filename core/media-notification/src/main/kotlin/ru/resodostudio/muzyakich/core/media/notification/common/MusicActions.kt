package ru.resodostudio.muzyakich.core.media.notification.common

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList
import ru.resodostudio.muzyakich.core.media.notification.R
import ru.resodostudio.muzyakich.core.media.notification.util.asNotificationAction
import ru.resodostudio.muzyakich.core.locales.R as localesR

@UnstableApi
internal object MusicActions {

    internal fun getSkipPreviousAction(
        context: Context,
        mediaSession: MediaSession,
        actionFactory: MediaNotification.ActionFactory,
    ) = MusicAction(
        iconResource = R.drawable.ic_outlined_skip_previous,
        titleResource = localesR.string.skip_previous,
        command = Player.COMMAND_SEEK_TO_PREVIOUS,
    ).asNotificationAction(context, mediaSession, actionFactory)

    internal fun getPlayPauseAction(
        context: Context,
        mediaSession: MediaSession,
        actionFactory: MediaNotification.ActionFactory,
        playWhenReady: Boolean,
    ) = MusicAction(
        iconResource = if (playWhenReady) R.drawable.ic_outlined_pause else R.drawable.ic_outlined_play_arrow,
        titleResource = if (playWhenReady) localesR.string.pause_audio else localesR.string.play_audio,
        command = Player.COMMAND_PLAY_PAUSE,
    ).asNotificationAction(context, mediaSession, actionFactory)

    internal fun getSkipNextAction(
        context: Context,
        mediaSession: MediaSession,
        actionFactory: MediaNotification.ActionFactory,
    ) = MusicAction(
        iconResource = R.drawable.ic_outlined_skip_next,
        titleResource = localesR.string.skip_next,
        command = Player.COMMAND_SEEK_TO_NEXT,
    ).asNotificationAction(context, mediaSession, actionFactory)

    internal fun getShuffleAction(
        mediaSession: MediaSession,
        customLayout: ImmutableList<CommandButton>,
        actionFactory: MediaNotification.ActionFactory,
    ) = actionFactory.createCustomActionFromCustomCommandButton(mediaSession, customLayout.first())
}