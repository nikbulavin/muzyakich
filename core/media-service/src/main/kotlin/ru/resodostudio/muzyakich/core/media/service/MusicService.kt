package ru.resodostudio.muzyakich.core.media.service

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.media.audiofx.AudioEffect
import androidx.annotation.OptIn
import androidx.core.os.bundleOf
import androidx.media3.cast.CastPlayer
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

        override fun onAudioSessionIdChanged(audioSessionId: Int) {
            val intent = Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION).apply {
                putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSessionId)
                putExtra(AudioEffect.EXTRA_PACKAGE_NAME, packageName)
                putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
            }
            sendBroadcast(intent)
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            mediaSession?.let { session ->
                updateMediaButtonPreferences(session, session.player)
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
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

        val exoPlayer = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()

        val castPlayer = CastPlayer.Builder(this)
            .setLocalPlayer(exoPlayer)
            .build()

        castPlayer.addListener(playerListener)

        val sessionActivityPendingIntent = TaskStackBuilder.create(this).run {
            addNextIntent(Intent(this@MusicService, Class.forName(TARGET_ACTIVITY_NAME)))
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        mediaSession = MediaSession.Builder(this, castPlayer)
            .setCallback(musicSessionCallback)
            .setSessionActivity(sessionActivityPendingIntent)
            .build()
            .also { mediaSession ->
                mediaSession.sessionExtras = bundleOf(
                    MediaConstants.EXTRAS_KEY_SLOT_RESERVATION_SEEK_TO_PREV to true,
                    MediaConstants.EXTRAS_KEY_SLOT_RESERVATION_SEEK_TO_NEXT to true,
                )
                updateMediaButtonPreferences(mediaSession, castPlayer)
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

        val repeatIcon = when (player.repeatMode) {
            Player.REPEAT_MODE_ALL -> CommandButton.ICON_REPEAT_ALL
            Player.REPEAT_MODE_ONE -> CommandButton.ICON_REPEAT_ONE
            else -> CommandButton.ICON_REPEAT_OFF
        }
        val repeatDisplayName = when (player.repeatMode) {
            Player.REPEAT_MODE_ALL -> getString(localesR.string.enable_repeat_mode_one)
            Player.REPEAT_MODE_ONE -> getString(localesR.string.disable_repeat_mode)
            else -> getString(localesR.string.enable_repeat_mode_all)
        }
        val nextRepeatMode = when (player.repeatMode) {
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_OFF
            else -> Player.REPEAT_MODE_ALL
        }

        val repeatButton = CommandButton.Builder(repeatIcon)
            .setDisplayName(repeatDisplayName)
            .setPlayerCommand(Player.COMMAND_SET_REPEAT_MODE, nextRepeatMode)
            .build()

        session.setMediaButtonPreferences(listOf(shuffleButton, repeatButton))
    }
}