package ru.resodostudio.muzyakich.ui.player

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
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
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledIconToggleButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Pause
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlayArrow
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.SkipNext
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.ui.component.SongArtworkMini
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NowPlayingBar(
    player: Player,
    currentSong: Song,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val motionScheme = MaterialTheme.motionScheme

    val shapes = remember {
        listOf(
            MaterialShapes.Pill,
            MaterialShapes.Pentagon,
            MaterialShapes.Gem,
            MaterialShapes.Sunny,
            MaterialShapes.VerySunny,
            MaterialShapes.Cookie4Sided,
            MaterialShapes.Cookie6Sided,
            MaterialShapes.Cookie7Sided,
            MaterialShapes.Cookie9Sided,
            MaterialShapes.Cookie12Sided,
            MaterialShapes.Clover8Leaf,
            MaterialShapes.SoftBurst,
            MaterialShapes.SoftBoom,
            MaterialShapes.Flower,
            MaterialShapes.Heart,
        )
    }

    var targetShape by remember { mutableStateOf(shapes.random()) }
    var previousShape by remember { mutableStateOf(targetShape) }
    val progress = remember { Animatable(1f) }

    LaunchedEffect(currentSong.mediaId) {
        previousShape = targetShape
        targetShape = shapes.random()

        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(500))
    }

    val morph = remember(previousShape, targetShape) {
        Morph(previousShape, targetShape)
    }

    val infiniteTransition = rememberInfiniteTransition("infinite outline movement")
    val currentRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "animatedRotation",
    )

    val currentShape = remember(morph, progress.value, currentRotation) {
        MorphPolygonShape(morph, progress.value, currentRotation)
    }

    Box(
        modifier = modifier
            .clickable { onClick() }
            .padding(horizontal = 14.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(vertical = 12.dp),
        ) {
            SongArtworkMini(
                artworkUri = currentSong.artworkUri,
                size = 46.dp,
                modifier = Modifier.clip(currentShape),
            )
            Spacer(Modifier.size(6.dp))
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
    val playPauseButtonState = rememberPlayPauseButtonState(player)
    val nextButtonState = rememberNextButtonState(player)
    MuzFilledIconToggleButton(
        checked = !playPauseButtonState.showPlay,
        onCheckedChange = { playPauseButtonState.onClick() },
        icon = if (playPauseButtonState.showPlay) MuzIcons.Rounded.PlayArrow else MuzIcons.Rounded.Pause,
        contentDescription = if (playPauseButtonState.showPlay) {
            stringResource(localesR.string.play_audio)
        } else {
            stringResource(localesR.string.pause_audio)
        },
        containerSize = smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide),
    )
    MuzIconButton(
        onClick = nextButtonState::onClick,
        modifier = modifier,
        enabled = nextButtonState.isEnabled,
        icon = MuzIcons.Rounded.SkipNext,
        contentDescription = stringResource(localesR.string.skip_next),
    )
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