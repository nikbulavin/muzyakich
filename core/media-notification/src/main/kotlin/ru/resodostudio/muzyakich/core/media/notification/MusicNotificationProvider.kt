package ru.resodostudio.muzyakich.core.media.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper.MediaStyle
import com.google.common.collect.ImmutableList
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.IO
import ru.resodostudio.muzyakich.core.common.di.ApplicationScope
import ru.resodostudio.muzyakich.core.media.notification.common.MusicActions
import ru.resodostudio.muzyakich.core.media.notification.util.asArtworkBitmap
import javax.inject.Inject
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(UnstableApi::class)
class MusicNotificationProvider @Inject constructor(
    @ApplicationScope private val appScope: CoroutineScope,
    @ApplicationContext private val context: Context,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : MediaNotification.Provider {

    private val notificationManager = checkNotNull(context.getSystemService<NotificationManager>())

    override fun createNotification(
        mediaSession: MediaSession,
        customLayout: ImmutableList<CommandButton>,
        actionFactory: MediaNotification.ActionFactory,
        onNotificationChangedCallback: MediaNotification.Provider.Callback,
    ): MediaNotification {
        ensureNotificationChannelExists()

        val player = mediaSession.player
        val metadata = player.mediaMetadata

        val builder = NotificationCompat.Builder(context, MUSIC_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(metadata.title)
            .setContentText(metadata.artist)
            .setSmallIcon(R.drawable.ic_outlined_play_arrow)
            .setStyle(MediaStyle(mediaSession))
            .setContentIntent(mediaSession.sessionActivity)

        getNotificationActions(
            mediaSession = mediaSession,
            customLayout = customLayout,
            actionFactory = actionFactory,
            playWhenReady = player.playWhenReady,
        ).forEach(builder::addAction)

        setupArtwork(
            uri = metadata.artworkUri,
            setLargeIcon = builder::setLargeIcon,
            updateNotification = {
                val notification = MediaNotification(MUSIC_NOTIFICATION_ID, builder.build())
                onNotificationChangedCallback.onNotificationChanged(notification)
            }
        )

        return MediaNotification(MUSIC_NOTIFICATION_ID, builder.build())
    }

    override fun handleCustomCommand(session: MediaSession, action: String, extras: Bundle) = false

    private fun ensureNotificationChannelExists() {

        if (notificationManager.getNotificationChannel(MUSIC_NOTIFICATION_CHANNEL_ID) != null) return

        val notificationChannel = NotificationChannel(
            MUSIC_NOTIFICATION_CHANNEL_ID,
            context.getString(localesR.string.music_notification_channel_name),
            NotificationManager.IMPORTANCE_LOW,
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun getNotificationActions(
        mediaSession: MediaSession,
        customLayout: ImmutableList<CommandButton>,
        actionFactory: MediaNotification.ActionFactory,
        playWhenReady: Boolean,
    ) = listOf(
        MusicActions.getSkipPreviousAction(context, mediaSession, actionFactory),
        MusicActions.getPlayPauseAction(context, mediaSession, actionFactory, playWhenReady),
        MusicActions.getSkipNextAction(context, mediaSession, actionFactory),
    )

    private fun setupArtwork(
        uri: Uri?,
        setLargeIcon: (Bitmap?) -> Unit,
        updateNotification: () -> Unit
    ) = appScope.launch {
        val bitmap = loadArtworkBitmap(uri)
        setLargeIcon(bitmap)
        updateNotification()
    }

    private suspend fun loadArtworkBitmap(uri: Uri?) =
        withContext(ioDispatcher) { uri?.asArtworkBitmap(context) }
}

private const val MUSIC_NOTIFICATION_ID = 1
private const val MUSIC_NOTIFICATION_CHANNEL_ID = ""
