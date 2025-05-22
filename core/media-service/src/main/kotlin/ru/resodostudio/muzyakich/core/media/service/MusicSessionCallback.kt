package ru.resodostudio.muzyakich.core.media.service

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import ru.resodostudio.muzyakich.core.common.Constants.PLAYBACK_MODE_REPEAT
import ru.resodostudio.muzyakich.core.common.Constants.PLAYBACK_MODE_REPEAT_ONE
import ru.resodostudio.muzyakich.core.common.Constants.PLAYBACK_MODE_SHUFFLE
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.Main
import ru.resodostudio.muzyakich.core.model.data.PlaybackMode
import javax.inject.Inject

class MusicSessionCallback @Inject constructor(
    @Dispatcher(Main) mainDispatcher: CoroutineDispatcher,
    private val musicActionHandler: MusicActionHandler,
) : MediaLibrarySession.Callback {
    private val coroutineScope = CoroutineScope(mainDispatcher + SupervisorJob())

    fun setPlaybackModeAction(playbackMode: PlaybackMode) {
        val actionsMap = mapOf(
            PlaybackMode.REPEAT to PLAYBACK_MODE_REPEAT,
            PlaybackMode.REPEAT_ONE to PLAYBACK_MODE_REPEAT_ONE,
            PlaybackMode.SHUFFLE to PLAYBACK_MODE_SHUFFLE
        )
        musicActionHandler.setRepeatShuffleCommand(actionsMap.getValue(playbackMode))
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: List<MediaItem>
    ): ListenableFuture<List<MediaItem>> = Futures.immediateFuture(
        mediaItems.map { mediaItem ->
            mediaItem.buildUpon()
                .setUri(mediaItem.requestMetadata.mediaUri)
                .build()
        }
    )

    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
        val connectionResult = super.onConnect(session, controller)
        val availableSessionCommands = connectionResult.availableSessionCommands.buildUpon()

        return MediaSession.ConnectionResult.accept(
            availableSessionCommands.build(),
            connectionResult.availablePlayerCommands
        )
    }

    override fun onPostConnect(session: MediaSession, controller: MediaSession.ControllerInfo) {
        session.setCustomLayout(controller, musicActionHandler.customLayout)
    }

    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
        musicActionHandler.onCustomCommand(mediaSession = session, customCommand = customCommand)
        session.setCustomLayout(musicActionHandler.customLayout)
        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
    }

    fun cancelCoroutineScope() {
        coroutineScope.cancel()
        musicActionHandler.cancelCoroutineScope()
    }
}
