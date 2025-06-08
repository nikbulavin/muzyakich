package ru.resodostudio.muzyakich.ui.library

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.smallContainerSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Pause
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlayArrow
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.SkipNext
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.core.model.data.PlaybackState
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun NowPlayingBar(
    nowPlayingState: NowPlayingState,
    song: Song,
    currentPosition: Long,
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onSkipNextClick: () -> Unit = {},
) {
    Surface(
        tonalElevation = 3.dp,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier,
    ) {
        Box {
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                headlineContent = {
                    Text(
                        text = song.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.basicMarquee(),
                    )
                },
                supportingContent = {
                    Text(
                        text = song.artist,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.basicMarquee(),
                    )
                },
                leadingContent = {
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(6.dp)),
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
                },
                trailingContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
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
                            modifier = Modifier
                                .size(smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
                        ) {
                            val defaultEffectsSpec =
                                MaterialTheme.motionScheme.defaultEffectsSpec<Float>()
                            AnimatedContent(
                                targetState = !nowPlayingState.playWhenReady,
                                label = "PlayPauseButton",
                                transitionSpec = {
                                    fadeIn() + scaleIn(
                                        defaultEffectsSpec,
                                        0.8f
                                    ) togetherWith fadeOut(snap())
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
                        ) {
                            Icon(
                                imageVector = MuzIcons.Rounded.SkipNext,
                                contentDescription = stringResource(localesR.string.skip_next),
                            )
                        }
                    }
                }
            )

            val progress by animateFloatAsState(
                targetValue = convertToProgress(
                    count = currentPosition,
                    total = song.duration,
                ),
                label = "ProgressAnimation",
                animationSpec = MaterialTheme.motionScheme.slowSpatialSpec(),
            )
            val progressModifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(3.dp)

            if (nowPlayingState.playbackState == PlaybackState.BUFFERING) {
                LinearProgressIndicator(
                    modifier = progressModifier,
                )
            } else {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = progressModifier,
                )
            }
        }
    }
}

private fun convertToProgress(count: Long, total: Long): Float {
    return (count * 100f / total / 100f).takeIf(Float::isFinite) ?: 0f
}