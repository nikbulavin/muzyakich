package ru.resodostudio.muzyakich.ui.player

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.extraSmallContainerSize
import androidx.compose.material3.IconButtonDefaults.largeContainerSize
import androidx.compose.material3.IconButtonDefaults.smallContainerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.OutlinedToggleButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import coil3.compose.SubcomposeAsyncImage
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.KeyboardArrowDown
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Pause
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlayArrow
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.QueueMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Repeat
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.RepeatOne
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Shuffle
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.SkipNext
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.SkipPrevious
import ru.resodostudio.muzyakich.core.designsystem.theme.LocalSharedTransitionScope
import ru.resodostudio.muzyakich.core.designsystem.theme.sharedElementTransitionSpec
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.core.model.data.PlaybackConfig
import ru.resodostudio.muzyakich.core.model.data.RepeatMode
import ru.resodostudio.muzyakich.core.model.data.RepeatMode.REPEAT_ALL
import ru.resodostudio.muzyakich.core.model.data.RepeatMode.REPEAT_OFF
import ru.resodostudio.muzyakich.core.model.data.RepeatMode.REPEAT_ONE
import ru.resodostudio.muzyakich.ui.component.LoadingState
import ru.resodostudio.muzyakich.ui.util.asFormattedString
import ru.resodostudio.muzyakich.ui.util.convertToProgress
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun PlayerScreen(
    onBackClick: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val playerUiState by viewModel.playerUiState.collectAsStateWithLifecycle()

    PlayerScreen(
        playerUiState = playerUiState,
        onBackClick = onBackClick,
        onSeekTo = viewModel::seekTo,
        onPlayClick = viewModel::play,
        onPauseClick = viewModel::pause,
        onSkipNextClick = viewModel::skipToNext,
        onSkipPreviousClick = viewModel::skipToPrevious,
        onShuffleToggle = viewModel::setShuffleModeEnabled,
        onRepeatToggle = viewModel::setRepeatMode,
    )
}

@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
private fun PlayerScreen(
    playerUiState: PlayerUiState,
    onBackClick: () -> Unit = {},
    onSeekTo: (Float) -> Unit = {},
    onPlayClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onSkipNextClick: () -> Unit = {},
    onSkipPreviousClick: () -> Unit = {},
    onShuffleToggle: (Boolean) -> Unit = {},
    onRepeatToggle: (RepeatMode) -> Unit = {},
) {
    val animatedVisibilityScope = LocalNavAnimatedContentScope.current
    val sharedTransitionScope = LocalSharedTransitionScope.current

    with(sharedTransitionScope) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            when (playerUiState) {
                PlayerUiState.Error -> LoadingState()
                PlayerUiState.Loading -> LoadingState()
                is PlayerUiState.Success -> {
                    val song = playerUiState.currentSong
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .systemBarsPadding()
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        FilledTonalIconButton(
                            onClick = onBackClick,
                            modifier = Modifier.size(extraSmallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
                            shapes = IconButtonDefaults.shapes(IconButtonDefaults.extraSmallRoundShape),
                        ) {
                            Icon(
                                imageVector = MuzIcons.Rounded.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 8.dp),
                            verticalArrangement = Arrangement.SpaceAround,
                        ) {
                            SubcomposeAsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .sharedBounds(
                                        boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                        sharedContentState = rememberSharedContentState(song.artworkUri),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    )
                                    .clip(RoundedCornerShape(18.dp)),
                                model = song.artworkUri,
                                contentDescription = null,
                                error = {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
                                    ) {
                                        Icon(
                                            imageVector = MuzIcons.Rounded.MusicNote,
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                },
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                            ) {
                                Text(
                                    text = song.title,
                                    maxLines = 1,
                                    modifier = Modifier
                                        .sharedBounds(
                                            boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                            sharedContentState = rememberSharedContentState(song.title),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                        )
                                        .basicMarquee(),
                                    style = MaterialTheme.typography.titleLarge,
                                )
                                Text(
                                    text = song.artist,
                                    maxLines = 1,
                                    modifier = Modifier
                                        .sharedBounds(
                                            boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                            sharedContentState = rememberSharedContentState(song.artist),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                        )
                                        .basicMarquee(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            ProgressSlider(
                                currentPosition = playerUiState.currentPosition,
                                duration = playerUiState.currentSong.duration,
                                onSeekTo = onSeekTo,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .sharedBounds(
                                        boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                        sharedContentState = rememberSharedContentState(song.mediaId),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    ),
                            )
                            PlayerActionButtons(
                                nowPlayingState = playerUiState.nowPlayingState,
                                onSkipPreviousClick = onSkipPreviousClick,
                                onPlayClick = onPlayClick,
                                onPauseClick = onPauseClick,
                                onSkipNextClick = onSkipNextClick,
                            )
                            PlaybackActionButtons(
                                playbackConfig = playerUiState.playbackConfig,
                                onRepeatToggle = onRepeatToggle,
                                onShuffleToggle = onShuffleToggle,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun PlaybackActionButtons(
    playbackConfig: PlaybackConfig,
    onRepeatToggle: (RepeatMode) -> Unit,
    onShuffleToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val repeatMode = playbackConfig.repeatMode
        OutlinedIconToggleButton(
            checked = repeatMode != REPEAT_OFF,
            onCheckedChange = { checked ->
                val newRepeatMode = when (repeatMode) {
                    REPEAT_OFF -> REPEAT_ALL
                    REPEAT_ALL -> REPEAT_ONE
                    REPEAT_ONE -> REPEAT_OFF
                }
                onRepeatToggle(newRepeatMode)
            },
            shapes = IconButtonDefaults.toggleableShapes(IconButtonDefaults.smallSquareShape),
            modifier = Modifier.size(smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
        ) {
            Icon(
                imageVector = if (repeatMode == REPEAT_ONE) MuzIcons.Rounded.RepeatOne else MuzIcons.Rounded.Repeat,
                contentDescription = null,
            )
        }

        OutlinedIconToggleButton(
            checked = playbackConfig.shuffleModeEnabled,
            onCheckedChange = onShuffleToggle,
            shapes = IconButtonDefaults.toggleableShapes(IconButtonDefaults.smallSquareShape),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
        ) {
            Icon(
                imageVector = MuzIcons.Rounded.Shuffle,
                contentDescription = stringResource(localesR.string.shuffle),
            )
        }

        var queueOpened by remember { mutableStateOf(false) }

        OutlinedToggleButton(
            checked = queueOpened,
            onCheckedChange = { queueOpened = it },
            shapes = ToggleButtonDefaults.shapes(ToggleButtonDefaults.squareShape),
            modifier = Modifier.height(40.dp),
        ) {
            Icon(
                imageVector = MuzIcons.Rounded.QueueMusic,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = ToggleButtonDefaults.IconSpacing)
                    .size(20.dp),
            )
            Text(stringResource(localesR.string.queue))
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun PlayerActionButtons(
    nowPlayingState: NowPlayingState,
    onSkipPreviousClick: () -> Unit,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FilledTonalIconButton(
            onClick = onSkipPreviousClick,
            shapes = IconButtonDefaults.shapes(),
            modifier = Modifier.size(largeContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)),
        ) {
            Icon(
                imageVector = MuzIcons.Rounded.SkipPrevious,
                contentDescription = stringResource(localesR.string.skip_previous),
                modifier = Modifier.size(32.dp),
            )
        }
        FilledIconButton(
            onClick = {
                if (!nowPlayingState.playWhenReady) {
                    onPlayClick()
                } else {
                    onPauseClick()
                }
            },
            shapes = IconButtonDefaults.shapes(),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(largeContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
        ) {
            val animSpec =
                MaterialTheme.motionScheme.slowEffectsSpec<Float>()
            AnimatedContent(
                targetState = !nowPlayingState.playWhenReady,
                label = "PlayPauseButton",
                transitionSpec = {
                    fadeIn(animSpec) + scaleIn(initialScale = 0.3f) togetherWith fadeOut(
                        animSpec
                    ) + scaleOut(targetScale = 0.3f)
                },
            ) { paused ->
                if (paused) {
                    Icon(
                        imageVector = MuzIcons.Rounded.PlayArrow,
                        contentDescription = stringResource(localesR.string.play_audio),
                        modifier = Modifier.size(32.dp),
                    )
                } else {
                    Icon(
                        imageVector = MuzIcons.Rounded.Pause,
                        contentDescription = stringResource(localesR.string.pause_audio),
                        modifier = Modifier.size(32.dp),
                    )
                }
            }
        }
        FilledTonalIconButton(
            onClick = onSkipNextClick,
            shapes = IconButtonDefaults.shapes(),
            enabled = nowPlayingState.hasNextMediaItem,
            modifier = Modifier.size(largeContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)),
        ) {
            Icon(
                imageVector = MuzIcons.Rounded.SkipNext,
                contentDescription = stringResource(localesR.string.skip_next),
                modifier = Modifier.size(32.dp),
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun ProgressSlider(
    currentPosition: Long,
    duration: Long,
    onSeekTo: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        val progress = convertToProgress(
            count = currentPosition,
            total = duration,
        )
        var sliderPosition by remember { mutableFloatStateOf(0f) }
        var isSeeking by remember { mutableStateOf(false) }
        if (!isSeeking) sliderPosition = progress
        Slider(
            value = sliderPosition,
            onValueChange = {
                isSeeking = true
                sliderPosition = it
                onSeekTo(it)
            },
            onValueChangeFinished = {
                onSeekTo(sliderPosition)
                isSeeking = false
            },
            modifier = modifier,
        )

        val timeMillis by remember(currentPosition) {
            derivedStateOf {
                val current = currentPosition.seconds
                val total = duration.seconds
                val remaining = (total - current).coerceAtLeast(Duration.ZERO)
                TimeMillis(
                    current.toLong(DurationUnit.SECONDS).asFormattedString(),
                    remaining.toLong(DurationUnit.SECONDS).asFormattedString(),
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = timeMillis.current,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "-${timeMillis.remaining}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private data class TimeMillis(val current: String, val remaining: String)