package ru.resodostudio.muzyakich.ui.player

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.smallContainerSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
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

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalSharedTransitionApi::class,
)
@Composable
fun NowPlayingBar(
    player: Player,
    currentSong: Song,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val motionScheme = MaterialTheme.motionScheme
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
            val animSpec = motionScheme.defaultSpatialSpec<IntOffset>()
            AnimatedContent(
                targetState = currentSong,
                transitionSpec = {
                    fadeIn() + slideInHorizontally(animSpec) { it / 16 } togetherWith
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

    if (progressState.bufferedPositionProgress == 0f) {
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

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
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