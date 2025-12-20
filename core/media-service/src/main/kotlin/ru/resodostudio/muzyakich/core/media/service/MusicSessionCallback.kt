package ru.resodostudio.muzyakich.core.media.service

import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionCommands
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import ru.resodostudio.muzyakich.core.common.Constants.REPEAT_MODE_ALL
import ru.resodostudio.muzyakich.core.common.Constants.REPEAT_MODE_OFF
import ru.resodostudio.muzyakich.core.common.Constants.REPEAT_MODE_ONE
import ru.resodostudio.muzyakich.core.common.Constants.SHUFFLE_MODE_OFF
import ru.resodostudio.muzyakich.core.common.Constants.SHUFFLE_MODE_ON
import ru.resodostudio.muzyakich.core.model.data.RepeatMode
import javax.inject.Inject

class MusicSessionCallback @Inject constructor(
    private val musicActionHandler: MusicActionHandler,
) : MediaSession.Callback {

    private val mediaNotificationSessionCommands: SessionCommands
        @OptIn(UnstableApi::class)
        get() = MediaSession.ConnectionResult.DEFAULT_SESSION_AND_LIBRARY_COMMANDS
            .buildUpon()
            .also { builder ->
                musicActionHandler.customCommands.values.forEach { commandButton ->
                    commandButton.sessionCommand?.let { builder.add(it) }
                }
            }
            .build()

    fun setRepeatModeAction(repeatMode: RepeatMode) {
        val action = when (repeatMode) {
            RepeatMode.REPEAT_OFF -> REPEAT_MODE_OFF
            RepeatMode.REPEAT_ALL -> REPEAT_MODE_ALL
            RepeatMode.REPEAT_ONE -> REPEAT_MODE_ONE
        }
        musicActionHandler.setRepeatCommand(action)
    }

    fun setShuffleModeAction(shuffleModeEnabled: Boolean) {
        val action = if (shuffleModeEnabled) SHUFFLE_MODE_ON else SHUFFLE_MODE_OFF
        musicActionHandler.setShuffleCommand(action)
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: List<MediaItem>,
    ): ListenableFuture<List<MediaItem>> = Futures.immediateFuture(
        mediaItems.map { mediaItem ->
            mediaItem.buildUpon()
                .setMediaMetadata(mediaItem.mediaMetadata)
                .setUri(mediaItem.requestMetadata.mediaUri)
                .build()
        }
    )

    @OptIn(UnstableApi::class)
    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
    ): MediaSession.ConnectionResult {
        if (
            session.isMediaNotificationController(controller) ||
            session.isAutomotiveController(controller) ||
            session.isAutoCompanionController(controller)
        ) {
            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setAvailableSessionCommands(mediaNotificationSessionCommands)
                .setMediaButtonPreferences(musicActionHandler.customLayout)
                .build()
        }
        return MediaSession.ConnectionResult.AcceptedResultBuilder(session).build()
    }

    override fun onPostConnect(session: MediaSession, controller: MediaSession.ControllerInfo) {
        session.setCustomLayout(controller, musicActionHandler.customLayout)
    }

    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle,
    ): ListenableFuture<SessionResult> {
        musicActionHandler.onCustomCommand(customCommand)
        session.setCustomLayout(musicActionHandler.customLayout)
        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
    }
}
