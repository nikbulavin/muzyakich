package ru.resodostudio.muzyakich.feature.player.impl.component

import androidx.annotation.OptIn
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.indicators.ProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlin.math.sin

@OptIn(UnstableApi::class)
@Composable
internal fun WavyProgressSlider(
    player: Player?,
    playWhenReady: Boolean,
    modifier: Modifier = Modifier,
    onValueChange: ((Float) -> Unit)? = null,
    onValueChangeFinished: (() -> Unit)? = null,
    scope: CoroutineScope = rememberCoroutineScope(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    waveAmplitude: Dp = 3.dp,
    waveLength: Dp = 40.dp,
    waveSpeed: Dp = waveLength,
    thumbRadius: Dp = 2.dp,
    thumbGap: Dp = 6.dp,
    scrubbingThrottleMs: Long = 150L,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    trackEndDotRadius: Dp = 1.dp,
    trackEndDotColor: Color = activeColor,
) {
    var sliderWidthPx by remember { mutableIntStateOf(0) }

    ProgressIndicator(player, totalTickCount = sliderWidthPx, scope) {
        var isDragging by remember { mutableStateOf(false) }
        var seekPosition by remember { mutableFloatStateOf(0f) }
        var lastScrubTime by remember { mutableLongStateOf(0L) }

        val currentValue = if (isDragging) seekPosition else currentPositionProgress

        val waveDurationMs = remember(waveLength, waveSpeed) {
            ((waveLength.value / waveSpeed.value) * 1000f)
                .toInt()
                .coerceAtLeast(16)
        }
        val phaseAnim = rememberInfiniteTransition(label = "Wave").animateFloat(
            initialValue = 0f,
            targetValue = (2 * Math.PI).toFloat(),
            animationSpec = infiniteRepeatable(tween(waveDurationMs, easing = LinearEasing)),
            label = "Phase",
        )
        val ampAnim = animateFloatAsState(
            targetValue = if (playWhenReady) waveAmplitude.value else 0f,
            animationSpec = tween(500, easing = FastOutSlowInEasing),
            label = "Amplitude",
        )

        Box(
            modifier = modifier
                .height(32.dp)
                .onSizeChanged { sliderWidthPx = it.width },
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.fillMaxWidth()) {
                val centerY = size.height / 2
                val thumbR = thumbRadius.toPx()
                val gap = thumbGap.toPx()
                val trackEnd = size.width - thumbR

                val thumbX = thumbR + (trackEnd - thumbR) * currentValue

                val inactiveGapExtra = 4.dp.toPx()
                val activeGapExtra = 2.dp.toPx()

                val inactiveStart = thumbX + gap + inactiveGapExtra
                if (inactiveStart < trackEnd) {
                    drawLine(
                        color = inactiveColor,
                        start = Offset(inactiveStart, centerY),
                        end = Offset(trackEnd, centerY),
                        strokeWidth = 6.dp.toPx(),
                        cap = StrokeCap.Round,
                    )
                    drawCircle(
                        color = trackEndDotColor,
                        radius = trackEndDotRadius.toPx(),
                        center = Offset(trackEnd, centerY),
                    )
                }

                val activeEnd = thumbX - gap - activeGapExtra
                if (activeEnd > thumbR) {
                    val phase = phaseAnim.value
                    val amp = ampAnim.value

                    val activeWidth = activeEnd - thumbR
                    val dampenDist = 16.dp.toPx()
                    val wavelengthPx = waveLength.toPx()
                    val waveFreq = (2f * Math.PI / wavelengthPx).toFloat()
                    val amplitudePx = amp * density

                    fun yAt(localX: Float): Float {
                        val t = ((activeWidth - localX) / dampenDist).coerceIn(0f, 1f)
                        val dampFactor = t * t * (3f - 2f * t)
                        return centerY + amplitudePx * dampFactor * sin(localX * waveFreq - phase)
                    }

                    val sampleStep = (wavelengthPx / 8f).coerceIn(6.dp.toPx(), 24.dp.toPx())
                    val xs = buildList {
                        add(0f)
                        var x = sampleStep
                        while (x < activeWidth) {
                            add(x)
                            x += sampleStep
                        }
                        add(activeWidth)
                    }

                    val path = Path().apply {
                        moveTo(thumbR + xs[0], yAt(xs[0]))
                        for (i in 1 until xs.size - 1) {
                            val currX = xs[i]
                            val nextX = xs[i + 1]
                            val currY = yAt(currX)
                            val midX = (currX + nextX) / 2f
                            val midY = (currY + yAt(nextX)) / 2f
                            quadraticTo(thumbR + currX, currY, thumbR + midX, midY)
                        }
                        val lastX = xs.last()
                        lineTo(thumbR + lastX, yAt(lastX))
                    }
                    drawPath(
                        path = path,
                        color = activeColor,
                        style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round),
                    )
                }
            }

            Slider(
                value = currentValue,
                onValueChange = {
                    isDragging = true
                    seekPosition = it
                    onValueChange?.invoke(it)

                    val now = System.currentTimeMillis()
                    if (now - lastScrubTime > scrubbingThrottleMs) {
                        updateCurrentPositionProgress(it)
                        lastScrubTime = now
                    }
                },
                onValueChangeFinished = {
                    updateCurrentPositionProgress(seekPosition)
                    isDragging = false
                    onValueChangeFinished?.invoke()
                },
                modifier = Modifier.fillMaxSize(),
                enabled = changingProgressEnabled,
                colors = SliderDefaults.colors(
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent,
                    disabledActiveTrackColor = Color.Transparent,
                    disabledInactiveTrackColor = Color.Transparent,
                    thumbColor = activeColor,
                    disabledThumbColor = activeColor.copy(alpha = 0.5f),
                ),
                interactionSource = interactionSource,
            )
        }
    }
}