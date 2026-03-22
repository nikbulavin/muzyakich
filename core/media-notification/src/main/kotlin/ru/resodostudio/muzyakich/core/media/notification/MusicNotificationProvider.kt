package ru.resodostudio.muzyakich.core.media.notification

import android.app.Notification
import android.content.Context
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@OptIn(UnstableApi::class)
class MusicNotificationProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : MediaNotification.Provider {

    private val defaultProvider = DefaultMediaNotificationProvider.Builder(context).build()

    override fun createNotification(
        mediaSession: MediaSession,
        customLayout: ImmutableList<CommandButton>,
        actionFactory: MediaNotification.ActionFactory,
        onNotificationChangedCallback: MediaNotification.Provider.Callback,
    ): MediaNotification {

        val wrappedCallback = MediaNotification.Provider.Callback { notification ->
            onNotificationChangedCallback.onNotificationChanged(replaceIcons(notification))
        }

        val defaultNotification = defaultProvider.createNotification(
            mediaSession, customLayout, actionFactory, wrappedCallback
        )

        return replaceIcons(defaultNotification)
    }

    override fun handleCustomCommand(session: MediaSession, action: String, extras: Bundle): Boolean {
        return defaultProvider.handleCustomCommand(session, action, extras)
    }

    override fun getNotificationChannelInfo(): MediaNotification.Provider.NotificationChannelInfo {
        return defaultProvider.notificationChannelInfo
    }

    private fun replaceIcons(mediaNotification: MediaNotification): MediaNotification {
        val builder = Notification.Builder.recoverBuilder(context, mediaNotification.notification)

        builder.setSmallIcon(R.drawable.core_media_notification_ic_muzyakich)

        if (mediaNotification.notification.getLargeIcon() == null) {
            val fallbackIcon = Icon.createWithResource(context, R.drawable.core_media_notification_ic_placeholder)
            builder.setLargeIcon(fallbackIcon)
        }

        return MediaNotification(mediaNotification.notificationId, builder.build())
    }
}