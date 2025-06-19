package ru.resodostudio.muzyakich.core.media.service

import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
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
import ru.resodostudio.muzyakich.core.common.Constants.REPEAT_MODE_ALL
import ru.resodostudio.muzyakich.core.common.Constants.REPEAT_MODE_OFF
import ru.resodostudio.muzyakich.core.common.Constants.REPEAT_MODE_ONE
import ru.resodostudio.muzyakich.core.common.Constants.SHUFFLE_MODE_OFF
import ru.resodostudio.muzyakich.core.common.Constants.SHUFFLE_MODE_ON
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.Main
import ru.resodostudio.muzyakich.core.model.data.RepeatMode
import javax.inject.Inject

class MusicSessionCallback @Inject constructor(
    @Dispatcher(Main) mainDispatcher: CoroutineDispatcher,
    private val musicActionHandler: MusicActionHandler,
) : MediaLibrarySession.Callback {

    private val coroutineScope = CoroutineScope(mainDispatcher + SupervisorJob())
    val customLayout: List<CommandButton> get() = musicActionHandler.customLayout

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
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
        val connectionResult = super.onConnect(session, controller)
        val availableSessionCommands = connectionResult.availableSessionCommands.buildUpon()
        musicActionHandler.customCommands.values.forEach { commandButton ->
            commandButton.sessionCommand?.let(availableSessionCommands::add)
        }

        return MediaSession.ConnectionResult.accept(
            availableSessionCommands.build(),
            connectionResult.availablePlayerCommands,
        )
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

    fun cancelCoroutineScope() {
        coroutineScope.cancel()
        musicActionHandler.cancelCoroutineScope()
    }
}
