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
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import dagger.hilt.android.AndroidEntryPoint
import ru.resodostudio.muzyakich.core.common.Constants.TARGET_ACTIVITY_NAME
import ru.resodostudio.muzyakich.core.media.notification.MusicNotificationProvider
import javax.inject.Inject
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(UnstableApi::class)
@AndroidEntryPoint
internal class MusicService : MediaLibraryService() {

    @Inject
    lateinit var musicNotificationProvider: MusicNotificationProvider

    @Inject
    lateinit var musicSessionCallback: MusicSessionCallback

    @Inject
    lateinit var musicServiceConnection: MusicServiceConnection

    @Inject
    lateinit var playCountTracker: PlayCountTracker

    private var mediaLibrarySession: MediaLibrarySession? = null

    private var exoPlayer: ExoPlayer? = null

    private val castPlayerListener = object : Player.Listener {

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            mediaLibrarySession?.let { session ->
                updateMediaButtonPreferences(session, session.player)
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            mediaLibrarySession?.let { session ->
                updateMediaButtonPreferences(session, session.player)
            }
        }

        override fun onEvents(player: Player, events: Player.Events) {
            if (Player.EVENT_MEDIA_ITEM_TRANSITION in events ||
                Player.EVENT_POSITION_DISCONTINUITY in events &&
                player.currentPosition < 1000L
            ) {
                playCountTracker.resetLastIncrementedMediaId()
            }
            playCountTracker.updatePlayCountTracking(player)
        }
    }

    private val exoPlayerListener = object : Player.Listener {

        override fun onAudioSessionIdChanged(audioSessionId: Int) {
            musicServiceConnection.updateAudioSessionId(audioSessionId)
            sendAudioEffectIntent(audioSessionId, true)
        }
    }

    override fun onCreate() {
        super.onCreate()
        initializePlayerAndSession()
        setMediaNotificationProvider(musicNotificationProvider)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaLibrarySession

    override fun onDestroy() {
        playCountTracker.cancel()
        mediaLibrarySession?.run {
            player.removeListener(castPlayerListener)
            exoPlayer?.removeListener(exoPlayerListener)
            
            val audioSessionId = exoPlayer?.audioSessionId ?: player.audioSessionId
            player.release()
            release()
            clearListener()
            musicServiceConnection.updateAudioSessionId(null)
            sendAudioEffectIntent(audioSessionId, false)
        }
        exoPlayer = null
        mediaLibrarySession = null
        super.onDestroy()
    }

    private fun initializePlayerAndSession() {
        val player = buildPlayer()

        player.addListener(castPlayerListener)

        val sessionActivityPendingIntent = TaskStackBuilder.create(this).run {
            addNextIntent(Intent(this@MusicService, Class.forName(TARGET_ACTIVITY_NAME)))
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        mediaLibrarySession = MediaLibrarySession.Builder(this, player, musicSessionCallback)
            .setSessionActivity(sessionActivityPendingIntent)
            .build()
            .also { mediaLibrarySession ->
                mediaLibrarySession.sessionExtras = bundleOf(
                    MediaConstants.EXTRAS_KEY_SLOT_RESERVATION_SEEK_TO_PREV to true,
                    MediaConstants.EXTRAS_KEY_SLOT_RESERVATION_SEEK_TO_NEXT to true,
                )
                updateMediaButtonPreferences(mediaLibrarySession, player)
            }
    }

    @OptIn(UnstableApi::class)
    private fun buildPlayer(): Player {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(USAGE_MEDIA)
            .build()

        val exoPlayerInstance = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()

        exoPlayerInstance.addListener(exoPlayerListener)
        exoPlayer = exoPlayerInstance

        return CastPlayer.Builder(this)
            .setLocalPlayer(exoPlayerInstance)
            .build()
    }

    private fun updateMediaButtonPreferences(session: MediaLibrarySession, player: Player) {
        val shuffleIcon = if (player.shuffleModeEnabled) {
            CommandButton.ICON_SHUFFLE_ON
        } else {
            CommandButton.ICON_SHUFFLE_OFF
        }
        val shuffleDisplayName = if (player.shuffleModeEnabled) {
            getString(localesR.string.core_locales_disable_shuffle_mode)
        } else {
            getString(localesR.string.core_locales_enable_shuffle_mode)
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
            Player.REPEAT_MODE_ALL -> getString(localesR.string.core_locales_enable_repeat_mode_one)
            Player.REPEAT_MODE_ONE -> getString(localesR.string.core_locales_disable_repeat_mode)
            else -> getString(localesR.string.core_locales_enable_repeat_mode_all)
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

        session.setMediaButtonPreferences(
            listOf(
                shuffleButton,
                repeatButton,
            )
        )
    }

    private fun sendAudioEffectIntent(sessionId: Int, open: Boolean) {
        val action = if (open) {
            AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION
        } else {
            AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION
        }

        val intent = Intent(action)
        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, sessionId)
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, packageName)
        intent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)

        sendBroadcast(intent)
    }
}
