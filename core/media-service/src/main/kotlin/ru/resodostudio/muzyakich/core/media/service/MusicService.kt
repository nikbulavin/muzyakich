package ru.resodostudio.muzyakich.core.media.service

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.media.audiofx.AudioEffect
import androidx.annotation.OptIn
import androidx.core.os.bundleOf
import androidx.media3.cast.CastPlayer
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC
import androidx.media3.common.C.USAGE_MEDIA
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ShuffleOrder
import androidx.media3.exoplayer.source.ShuffleOrder.DefaultShuffleOrder
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
        exoPlayerInstance.shuffleOrder = CustomShuffleOrder(0)
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

    @UnstableApi
    private class CustomShuffleOrder(
        private val indices: IntArray,
        private val randomSeed: Long = 0L,
    ) : DefaultShuffleOrder(indices, randomSeed) {

        constructor(length: Int, randomSeed: Long = 0L) : this(
            extractIndices(DefaultShuffleOrder(length, randomSeed)),
            randomSeed,
        )

        override fun cloneAndInsert(insertionIndex: Int, insertionCount: Int): ShuffleOrder {
            val base = super.cloneAndInsert(insertionIndex, insertionCount) as DefaultShuffleOrder
            return CustomShuffleOrder(extractIndices(base), randomSeed)
        }

        override fun cloneAndRemove(indexFrom: Int, indexToExclusive: Int): ShuffleOrder {
            val base = super.cloneAndRemove(indexFrom, indexToExclusive) as DefaultShuffleOrder
            return CustomShuffleOrder(extractIndices(base), randomSeed)
        }

        override fun cloneAndClear(): ShuffleOrder {
            return CustomShuffleOrder(IntArray(0), randomSeed)
        }

        override fun cloneAndMove(indexFrom: Int, indexToExclusive: Int, newIndexFrom: Int): ShuffleOrder {
            if (indexToExclusive - indexFrom != 1) {
                val base = super.cloneAndMove(indexFrom, indexToExclusive, newIndexFrom) as DefaultShuffleOrder
                return CustomShuffleOrder(extractIndices(base), randomSeed)
            }

            val pFrom = indices.indexOf(indexFrom)
            val pTo = indices.indexOf(newIndexFrom)

            val pMapped = IntArray(indices.size)
            for (i in indices.indices) {
                val x = indices[i]
                pMapped[i] = when {
                    x == indexFrom -> newIndexFrom
                    indexFrom > newIndexFrom && x in newIndexFrom until indexFrom -> x + 1
                    indexFrom < newIndexFrom && x in (indexFrom + 1)..newIndexFrom -> x - 1
                    else -> x
                }
            }

            if (pFrom != -1 && pTo != -1) {
                val list = pMapped.toMutableList()
                val item = list.removeAt(pFrom)
                list.add(pTo, item)
                return CustomShuffleOrder(list.toIntArray(), randomSeed)
            }

            return CustomShuffleOrder(pMapped, randomSeed)
        }

        companion object {
            private fun extractIndices(order: DefaultShuffleOrder): IntArray {
                val indices = IntArray(order.length)
                var index = order.firstIndex
                var i = 0
                while (index != C.INDEX_UNSET) {
                    indices[i++] = index
                    index = order.getNextIndex(index)
                }
                return indices
            }
        }
    }
}
