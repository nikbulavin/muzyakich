package ru.resodostudio.muzyakich.core.media.service

import androidx.media3.common.C
import androidx.media3.common.Player
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers
import ru.resodostudio.muzyakich.core.common.di.ApplicationScope
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

internal class PlayCountTracker @Inject constructor(
    private val songsRepository: SongsRepository,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @Dispatcher(MuzDispatchers.Main) private val mainDispatcher: CoroutineDispatcher,
) {
    private var playCountJob: Job? = null
    private var lastIncrementedMediaId: String? = null

    fun resetLastIncrementedMediaId() {
        lastIncrementedMediaId = null
    }

    fun updatePlayCountTracking(player: Player) {
        val currentMediaId = player.currentMediaItem?.mediaId
        if (currentMediaId == null || currentMediaId == lastIncrementedMediaId) {
            playCountJob?.cancel()
            playCountJob = null
            return
        }

        if (player.playWhenReady && player.playbackState == Player.STATE_READY) {
            if (playCountJob?.isActive != true) {
                playCountJob = applicationScope.launch(mainDispatcher) {
                    val duration = player.duration
                    val threshold = if (duration != C.TIME_UNSET && duration in 1..<30_000L) duration else 30_000L

                    while (true) {
                        val currentPosition = player.currentPosition

                        if (currentPosition >= threshold) {
                            lastIncrementedMediaId = currentMediaId
                            songsRepository.incrementPlayCount(currentMediaId)
                            break
                        }

                        val remainingMs = threshold - currentPosition
                        val speed = player.playbackParameters.speed

                        val delayMs = if (speed > 0f) (remainingMs / speed).toLong() else 1000L

                        delay(delayMs.coerceAtLeast(100L).milliseconds)
                    }
                }
            }
        } else {
            playCountJob?.cancel()
            playCountJob = null
        }
    }

    fun cancel() {
        playCountJob?.cancel()
        playCountJob = null
    }
}
