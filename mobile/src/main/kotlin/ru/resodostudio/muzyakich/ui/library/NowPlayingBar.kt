package ru.resodostudio.muzyakich.ui.library

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.smallContainerSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.rectangle
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Pause
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlayArrow
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.SkipNext
import ru.resodostudio.muzyakich.core.designsystem.theme.LocalSharedTransitionScope
import ru.resodostudio.muzyakich.core.designsystem.theme.SharedElementKey
import ru.resodostudio.muzyakich.core.designsystem.theme.sharedBoundsRevealWithShapeMorph
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.core.model.data.PlaybackState
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.ui.component.SongArtworkMini
import ru.resodostudio.muzyakich.ui.util.convertToProgress
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalSharedTransitionApi::class,
)
@Composable
internal fun NowPlayingBar(
    nowPlayingState: NowPlayingState,
    song: Song,
    currentPosition: Long,
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onSkipNextClick: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    with(LocalSharedTransitionScope.current) {
        Surface(
            tonalElevation = 3.dp,
            modifier = modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable { onClick() }
                .sharedBoundsRevealWithShapeMorph(
                    sharedContentState = rememberSharedContentState(SharedElementKey.NowPlayingBarToPlayerScreen),
                    restingShape = RoundedPolygon.rectangle(rounding = CornerRounding(12f)),
                    targetShape = RoundedPolygon.rectangle().normalized(),
                    targetValueByState = {
                        when (it) {
                            EnterExitState.PreEnter -> 0f
                            EnterExitState.Visible -> 1f
                            EnterExitState.PostExit -> 1f
                        }
                    },
                ),
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 14.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(vertical = 12.dp),
                ) {
                    val animSpec = MaterialTheme.motionScheme.defaultSpatialSpec<IntOffset>()
                    AnimatedContent(
                        targetState = song,
                        transitionSpec = {
                            fadeIn() + slideInHorizontally(animSpec) { it / 16 } togetherWith fadeOut(
                                snap()
                            )
                        },
                        contentKey = { it.mediaId },
                        modifier = Modifier.weight(1f),
                    ) { songState ->
                        SongInfo(
                            song = songState,
                        )
                    }
                    ActionButtons(
                        nowPlayingState = nowPlayingState,
                        onPlayClick = onPlayClick,
                        onPauseClick = onPauseClick,
                        onSkipNextClick = onSkipNextClick,
                    )
                }

                SongProgressIndicator(
                    currentPosition = currentPosition,
                    song = song,
                    nowPlayingState = nowPlayingState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(3.dp),
                )
            }
        }
    }
}

@Composable
private fun SongInfo(
    song: Song,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SongArtworkMini(
            artworkUri = song.artworkUri,
            size = 46.dp,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(bottom = 2.dp, end = 12.dp),
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
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun SongProgressIndicator(
    currentPosition: Long,
    song: Song,
    nowPlayingState: NowPlayingState,
    modifier: Modifier = Modifier,
) {
    val progress by animateFloatAsState(
        targetValue = convertToProgress(
            count = currentPosition,
            total = song.duration,
        ),
        label = "ProgressAnimation",
        animationSpec = MaterialTheme.motionScheme.slowSpatialSpec(),
    )

    if (nowPlayingState.playbackState == PlaybackState.BUFFERING) {
        LinearProgressIndicator(
            modifier = modifier,
        )
    } else {
        LinearProgressIndicator(
            progress = { progress },
            modifier = modifier,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun ActionButtons(
    nowPlayingState: NowPlayingState,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledIconButton(
        onClick = {
            if (!nowPlayingState.playWhenReady) {
                onPlayClick()
            } else {
                onPauseClick()
            }
        },
        shapes = IconButtonDefaults.shapes(),
        modifier = modifier
            .size(smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
    ) {
        val animSpec = MaterialTheme.motionScheme.slowEffectsSpec<Float>()
        AnimatedContent(
            targetState = !nowPlayingState.playWhenReady,
            label = "PlayPauseButton",
            transitionSpec = {
                fadeIn(animSpec) + scaleIn(initialScale = 0.3f) togetherWith fadeOut(animSpec) + scaleOut(
                    targetScale = 0.3f
                )
            },
        ) { paused ->
            if (paused) {
                Icon(
                    imageVector = MuzIcons.Rounded.PlayArrow,
                    contentDescription = stringResource(localesR.string.play_audio),
                )
            } else {
                Icon(
                    imageVector = MuzIcons.Rounded.Pause,
                    contentDescription = stringResource(localesR.string.pause_audio),
                )
            }
        }
    }
    IconButton(
        onClick = onSkipNextClick,
        shapes = IconButtonDefaults.shapes(),
        modifier = modifier,
        enabled = nowPlayingState.hasNextMediaItem,
    ) {
        Icon(
            imageVector = MuzIcons.Rounded.SkipNext,
            contentDescription = stringResource(localesR.string.skip_next),
        )
    }
}