package ru.resodostudio.muzyakich.core.media.service

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import androidx.annotation.OptIn
import androidx.core.os.bundleOf
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC
import androidx.media3.common.C.USAGE_MEDIA
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaConstants
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import ru.resodostudio.muzyakich.core.common.Constants.TARGET_ACTIVITY_NAME
import ru.resodostudio.muzyakich.core.media.notification.MusicNotificationProvider
import javax.inject.Inject
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(UnstableApi::class)
@AndroidEntryPoint
class MusicService : MediaSessionService() {

    @Inject
    lateinit var musicNotificationProvider: MusicNotificationProvider

    @Inject
    lateinit var musicSessionCallback: MusicSessionCallback

    private var mediaSession: MediaSession? = null

    private val playerListener = object : Player.Listener {

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            mediaSession?.let { session ->
                updateMediaButtonPreferences(session, session.player)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        initializePlayerAndSession()
        setMediaNotificationProvider(musicNotificationProvider)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    override fun onDestroy() {
        mediaSession?.run {
            player.removeListener(playerListener)
            player.release()
            release()
            clearListener()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        pauseAllPlayersAndStopSelf()
    }

    private fun initializePlayerAndSession() {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(USAGE_MEDIA)
            .build()

        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()

        player.addListener(playerListener)

        val sessionActivityPendingIntent = TaskStackBuilder.create(this).run {
            addNextIntent(Intent(this@MusicService, Class.forName(TARGET_ACTIVITY_NAME)))
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        mediaSession = MediaSession.Builder(this, player)
            .setCallback(musicSessionCallback)
            .setSessionActivity(sessionActivityPendingIntent)
            .build()
            .also { mediaSession ->
                mediaSession.sessionExtras = bundleOf(
                    MediaConstants.EXTRAS_KEY_SLOT_RESERVATION_SEEK_TO_PREV to true,
                    MediaConstants.EXTRAS_KEY_SLOT_RESERVATION_SEEK_TO_NEXT to true,
                )
                updateMediaButtonPreferences(mediaSession, player)
            }
    }

    private fun updateMediaButtonPreferences(session: MediaSession, player: Player) {
        val shuffleIcon = if (player.shuffleModeEnabled) {
            CommandButton.ICON_SHUFFLE_ON
        } else {
            CommandButton.ICON_SHUFFLE_OFF
        }
        val shuffleDisplayName = if (player.shuffleModeEnabled) {
            getString(localesR.string.disable_shuffle_mode)
        } else {
            getString(localesR.string.enable_shuffle_mode)
        }

        val shuffleButton = CommandButton.Builder(shuffleIcon)
            .setDisplayName(shuffleDisplayName)
            .setPlayerCommand(Player.COMMAND_SET_SHUFFLE_MODE)
            .build()

        session.setMediaButtonPreferences(listOf(shuffleButton))
    }
}