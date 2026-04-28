package ru.resodostudio.muzyakich.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.smallContainerSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toPath
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.Morph
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberNextButtonState
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberProgressStateWithTickCount
import ru.resodostudio.cashsense.core.ui.SongArtworkMini
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledIconToggleButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Pause
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlayArrow
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.SkipNext
import ru.resodostudio.muzyakich.core.model.data.Song
import kotlin.math.roundToInt
import ru.resodostudio.muzyakich.core.locales.R as localesR

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NowPlayingBar(
    player: Player,
    currentSong: Song,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val motionScheme = MaterialTheme.motionScheme

    val playPauseButtonState = rememberPlayPauseButtonState(player)
    val isPlaying = !playPauseButtonState.showPlay

    var lastArtworkShape by remember { mutableStateOf(artworkShapes.random()) }
    var targetShape by remember { mutableStateOf(if (isPlaying) lastArtworkShape else MaterialShapes.Square) }
    var previousShape by remember { mutableStateOf(targetShape) }
    val progress = remember { Animatable(1f) }

    LaunchedEffect(currentSong.mediaId, isPlaying) {
        val newTarget = if (isPlaying) {
            artworkShapes.filter { it != lastArtworkShape }.random().also { lastArtworkShape = it }
        } else {
            MaterialShapes.Square
        }
        previousShape = targetShape
        targetShape = newTarget

        progress.snapTo(0f)
        progress.animateTo(1f, motionScheme.slowSpatialSpec())
    }

    val morph = remember(previousShape, targetShape) {
        Morph(previousShape, targetShape)
    }

    val currentRotation = remember { Animatable(0f) }
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (true) {
                val remainingRotation = 360f - currentRotation.value
                val duration = (9000 * (remainingRotation / 360f)).toInt().coerceAtLeast(1)
                currentRotation.animateTo(
                    targetValue = 360f,
                    animationSpec = tween(
                        durationMillis = duration,
                        easing = LinearEasing,
                    ),
                )
                currentRotation.snapTo(0f)
            }
        } else {
            val targetAngle = (currentRotation.value / 90f).roundToInt() * 90f
            currentRotation.animateTo(targetAngle, motionScheme.slowSpatialSpec())
            currentRotation.snapTo(currentRotation.value % 360f)
        }
    }

    Box(
        modifier = modifier
            .clickable { onClick() }
            .padding(horizontal = 14.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            val shadowColor = MaterialTheme.colorScheme.inverseSurface
            SongArtworkMini(
                artworkUri = currentSong.artworkUri,
                modifier = Modifier
                    .graphicsLayer {
                        clip = true
                        shape = MorphPolygonShape(
                            morph = morph,
                            percentage = progress.value,
                            rotation = currentRotation.value,
                        )
                        shadowElevation = 2.dp.toPx()
                        ambientShadowColor = shadowColor
                        spotShadowColor = shadowColor
                    }
                    .size(40.dp),
            )
            Spacer(Modifier.size(4.dp))
            AnimatedContent(
                targetState = currentSong,
                transitionSpec = {
                    fadeIn(motionScheme.fastEffectsSpec()) +
                            slideInHorizontally(motionScheme.fastSpatialSpec()) { it / 12 } togetherWith
                            fadeOut(snap())
                },
                contentKey = { it.mediaId },
                modifier = Modifier.weight(1f),
            ) { songState ->
                SongInfo(
                    song = songState,
                )
            }
            ActionButtons(
                player = player,
            )
        }
        SongProgressIndicator(
            player = player,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .height(3.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SongInfo(
    song: Song,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier.padding(bottom = 2.dp, end = 12.dp),
    ) {
        Text(
            text = song.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.basicMarquee(),
        )
        Text(
            text = song.artist,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.basicMarquee(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun SongProgressIndicator(
    player: Player,
    modifier: Modifier = Modifier,
) {
    val progressState = rememberProgressStateWithTickCount(player = player, totalTickCount = 2000)
    val progress by animateFloatAsState(
        targetValue = progressState.currentPositionProgress,
        label = "ProgressAnimation",
        animationSpec = MaterialTheme.motionScheme.slowSpatialSpec(),
    )

    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier,
    )
}

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ActionButtons(
    player: Player,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val playPauseButtonState = rememberPlayPauseButtonState(player)
        val nextButtonState = rememberNextButtonState(player)
        MuzFilledIconToggleButton(
            checked = !playPauseButtonState.showPlay,
            onCheckedChange = { playPauseButtonState.onClick() },
            icon = if (playPauseButtonState.showPlay) MuzIcons.Rounded.PlayArrow else MuzIcons.Rounded.Pause,
            contentDescription = if (playPauseButtonState.showPlay) {
                stringResource(localesR.string.core_locales_play_audio)
            } else {
                stringResource(localesR.string.core_locales_pause_audio)
            },
            containerSize = smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide),
        )
        MuzIconButton(
            onClick = nextButtonState::onClick,
            modifier = modifier,
            enabled = nextButtonState.isEnabled,
            icon = MuzIcons.Rounded.SkipNext,
            contentDescription = stringResource(localesR.string.core_locales_skip_next),
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private class MorphPolygonShape(
    private val morph: Morph,
    private val percentage: Float,
    private val rotation: Float = 0f,
) : Shape {
    private val matrix = Matrix()
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        matrix.reset()
        matrix.scale(size.width, size.height)
        matrix.translate(0.5f, 0.5f)
        matrix.rotateZ(rotation)
        matrix.translate(-0.5f, -0.5f)

        val path = morph.toPath(progress = percentage)
        path.transform(matrix)
        path.translate(size.center - path.getBounds().center)
        return Outline.Generic(path)
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private val artworkShapes = listOf(
    MaterialShapes.Pill,
    MaterialShapes.Pentagon,
    MaterialShapes.Gem,
    MaterialShapes.VerySunny,
    MaterialShapes.Cookie4Sided,
    MaterialShapes.Cookie6Sided,
    MaterialShapes.Cookie7Sided,
    MaterialShapes.Cookie9Sided,
    MaterialShapes.Cookie12Sided,
    MaterialShapes.Clover8Leaf,
    MaterialShapes.Heart,
)