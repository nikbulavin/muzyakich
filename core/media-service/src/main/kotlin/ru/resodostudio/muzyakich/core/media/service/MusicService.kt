package ru.resodostudio.muzyakich.core.media.service

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC
import androidx.media3.common.C.USAGE_MEDIA
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.common.Constants.TARGET_ACTIVITY_NAME
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.Main
import ru.resodostudio.muzyakich.core.data.repository.UserDataRepository
import ru.resodostudio.muzyakich.core.media.notification.MusicNotificationProvider
import ru.resodostudio.muzyakich.core.media.service.util.unsafeLazy
import ru.resodostudio.muzyakich.core.model.data.RepeatMode.REPEAT_ALL
import ru.resodostudio.muzyakich.core.model.data.RepeatMode.REPEAT_OFF
import ru.resodostudio.muzyakich.core.model.data.RepeatMode.REPEAT_ONE
import javax.inject.Inject

@OptIn(UnstableApi::class)
@AndroidEntryPoint
class MusicService : MediaSessionService() {

    @Inject
    @Dispatcher(Main)
    lateinit var mainDispatcher: CoroutineDispatcher

    @Inject
    lateinit var userDataRepository: UserDataRepository

    @Inject
    lateinit var musicNotificationProvider: MusicNotificationProvider

    @Inject
    lateinit var musicSessionCallback: MusicSessionCallback

    private var mediaSession: MediaSession? = null

    private val coroutineScope by unsafeLazy { CoroutineScope(mainDispatcher + SupervisorJob()) }

    private val _currentMediaId = MutableStateFlow("")

    override fun onCreate() {
        super.onCreate()

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(USAGE_MEDIA)
            .build()

        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()

        val sessionActivityPendingIntent = TaskStackBuilder.create(this).run {
            addNextIntent(Intent(this@MusicService, Class.forName(TARGET_ACTIVITY_NAME)))
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        mediaSession = MediaSession.Builder(this, player)
            .setCallback(musicSessionCallback)
            .setSessionActivity(sessionActivityPendingIntent)
            .build()
            .apply { player.addListener(PlayerListener()) }

        setMediaNotificationProvider(musicNotificationProvider)
        setAudioOffloadPreferences(player)

        startPlaybackModeSync()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        musicSessionCallback.cancelCoroutineScope()
        musicNotificationProvider.cancelCoroutineScope()
        super.onDestroy()
    }

    private fun startPlaybackModeSync() = coroutineScope.launch {
        userDataRepository.userData.collectLatest { userData ->
            val playbackConfig = userData.playbackConfig
            mediaSession?.player?.run {
                repeatMode = when (playbackConfig.repeatMode) {
                    REPEAT_OFF -> Player.REPEAT_MODE_OFF
                    REPEAT_ALL -> Player.REPEAT_MODE_ALL
                    REPEAT_ONE -> Player.REPEAT_MODE_ONE
                }
                shuffleModeEnabled = playbackConfig.shuffleModeEnabled
            }
            musicSessionCallback.setRepeatModeAction(playbackConfig.repeatMode)
            musicSessionCallback.setShuffleModeAction(playbackConfig.shuffleModeEnabled)
            mediaSession?.setCustomLayout(musicSessionCallback.customLayout)
        }
    }

    private fun setAudioOffloadPreferences(player: ExoPlayer) {
        val audioOffloadPreferences =
            TrackSelectionParameters.AudioOffloadPreferences.Builder()
                .setAudioOffloadMode(TrackSelectionParameters.AudioOffloadPreferences.AUDIO_OFFLOAD_MODE_ENABLED)
                .setIsGaplessSupportRequired(true)
                .build()
        player.trackSelectionParameters =
            player.trackSelectionParameters
                .buildUpon()
                .setAudioOffloadPreferences(audioOffloadPreferences)
                .build()
    }

    private inner class PlayerListener : Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            if (mediaItem == null) return
            _currentMediaId.update { mediaItem.mediaId }
        }
    }
}