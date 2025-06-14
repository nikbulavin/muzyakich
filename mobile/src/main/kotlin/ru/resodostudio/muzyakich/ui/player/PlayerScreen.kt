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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.largeContainerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Pause
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlayArrow
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.SkipNext
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.SkipPrevious
import ru.resodostudio.muzyakich.core.designsystem.theme.LocalSharedTransitionScope
import ru.resodostudio.muzyakich.core.designsystem.theme.sharedElementTransitionSpec
import ru.resodostudio.muzyakich.ui.component.LoadingState
import ru.resodostudio.muzyakich.ui.util.asFormattedString
import ru.resodostudio.muzyakich.ui.util.convertToProgress
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val playerUiState by viewModel.playerUiState.collectAsStateWithLifecycle()

    PlayerScreen(
        playerUiState = playerUiState,
        onSeekTo = viewModel::seekTo,
        onPlayClick = viewModel::play,
        onPauseClick = viewModel::pause,
        onSkipNextClick = viewModel::skipToNext,
        onSkipPreviousClick = viewModel::skipToPrevious,
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
    onSeekTo: (Float) -> Unit = {},
    onPlayClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onSkipNextClick: () -> Unit = {},
    onSkipPreviousClick: () -> Unit = {},
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
                            .statusBarsPadding()
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.Center,
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
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 32.dp),
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
                                    .padding(bottom = 16.dp)
                                    .sharedBounds(
                                        boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                        sharedContentState = rememberSharedContentState(song.artist),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    )
                                    .basicMarquee(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                            val progress = convertToProgress(
                                count = playerUiState.currentPosition,
                                total = song.duration,
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                                    .sharedBounds(
                                        boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                        sharedContentState = rememberSharedContentState(song.mediaId),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    ),
                            )

                            val timeMillis by remember(playerUiState.currentPosition) {
                                derivedStateOf {
                                    val current = playerUiState.currentPosition.seconds
                                    val total = playerUiState.nowPlayingState.duration.seconds
                                    val remaining = (total - current).coerceAtLeast(Duration.ZERO)
                                    TimeMillis(
                                        current.toLong(DurationUnit.SECONDS).asFormattedString(),
                                        remaining.toLong(DurationUnit.SECONDS).asFormattedString(),
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 32.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = timeMillis.current,
                                    style = MaterialTheme.typography.labelMedium,
                                )
                                Text(
                                    text = "-${timeMillis.remaining}",
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
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
                                        if (!playerUiState.nowPlayingState.playWhenReady) {
                                            onPlayClick()
                                        } else {
                                            onPauseClick()
                                        }
                                    },
                                    shapes = IconButtonDefaults.shapes(),
                                    modifier = Modifier
                                        .size(largeContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide))
                                        .padding(horizontal = 8.dp),
                                ) {
                                    val animSpec =
                                        MaterialTheme.motionScheme.slowEffectsSpec<Float>()
                                    AnimatedContent(
                                        targetState = !playerUiState.nowPlayingState.playWhenReady,
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
                                    enabled = playerUiState.nowPlayingState.hasNextMediaItem,
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
                    }
                }
            }
        }
    }
}

private data class TimeMillis(val current: String, val remaining: String)