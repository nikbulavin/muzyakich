package ru.resodostudio.muzyakich.core.ui.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

fun Modifier.fadedMarquee(
    fadeWidth: Dp = 16.dp,
    iterations: Int = Int.MAX_VALUE,
    repeatDelayMillis: Int = 1500,
    initialDelayMillis: Int = 1200,
    spacing: Dp = 32.dp,
    velocity: Dp = 30.dp,
): Modifier = composed {
    val fadeAnimSpec = MaterialTheme.motionScheme.slowEffectsSpec<Float>()
    val density = LocalDensity.current
    val fadeWidthPx = with(density) { fadeWidth.toPx() }
    val spacingPx = with(density) { spacing.toPx() }
    val velocityPx = with(density) { velocity.toPx() }

    var containerWidth by remember { mutableIntStateOf(0) }
    var contentWidth by remember { mutableIntStateOf(0) }

    val offset = remember { Animatable(0f) }
    val leftFadeAlpha = remember { Animatable(0f) }

    LaunchedEffect(contentWidth, containerWidth, iterations) {
        if (containerWidth !in 1..<contentWidth) {
            offset.snapTo(0f)
            leftFadeAlpha.snapTo(0f)
            return@LaunchedEffect
        }

        val distance = contentWidth + spacingPx
        val durationMillis = ((distance / velocityPx) * 1000).toInt().coerceAtLeast(1)

        offset.snapTo(0f)
        leftFadeAlpha.snapTo(0f)
        delay(initialDelayMillis.toLong().milliseconds)

        var iteration = 0
        while (iteration < iterations && isActive) {
            launch {
                leftFadeAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = fadeAnimSpec,
                )
            }

            offset.animateTo(
                targetValue = -distance,
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing,
                ),
            )

            leftFadeAlpha.animateTo(
                targetValue = 0f,
                animationSpec = fadeAnimSpec,
            )

            offset.snapTo(0f)
            iteration++

            if (iteration < iterations && isActive) {
                delay(repeatDelayMillis.toLong().milliseconds)
            }
        }
    }

    this
        .graphicsLayer(
            compositingStrategy = CompositingStrategy.Offscreen,
            clip = true,
        )
        .layout { measurable, constraints ->
            val looseConstraints = constraints.copy(minWidth = 0, maxWidth = Constraints.Infinity)
            val placeable = measurable.measure(looseConstraints)

            contentWidth = placeable.width
            val layoutWidth = constraints.constrainWidth(placeable.width)
            containerWidth = if (constraints.hasBoundedWidth) constraints.maxWidth else layoutWidth

            layout(layoutWidth, placeable.height) {
                placeable.placeRelative(0, 0)
            }
        }
        .drawWithContent {
            val isOverflowing = containerWidth in 1..<contentWidth
            if (!isOverflowing) {
                drawContent()
                return@drawWithContent
            }

            val currentOffset = offset.value
            val distance = contentWidth + spacingPx

            translate(left = currentOffset) {
                this@drawWithContent.drawContent()
            }
            if (currentOffset < 0f) {
                translate(left = currentOffset + distance) {
                    this@drawWithContent.drawContent()
                }
            }

            val visibleWidth = containerWidth.toFloat()
            if (fadeWidthPx > 0f && visibleWidth > 0f) {
                val leftAlpha = leftFadeAlpha.value
                val leftStop = (fadeWidthPx / visibleWidth).coerceIn(0f, 0.5f)
                val rightStop = (1f - (fadeWidthPx / visibleWidth)).coerceIn(0.5f, 1f)

                drawRect(
                    brush = Brush.horizontalGradient(
                        0.0f to Color.Black.copy(alpha = 1f - leftAlpha),
                        leftStop to Color.Black,
                        rightStop to Color.Black,
                        1.0f to Color.Transparent,
                        startX = 0f,
                        endX = visibleWidth,
                    ),
                    size = size.copy(width = visibleWidth),
                    blendMode = BlendMode.DstIn,
                )
            }
        }
}
