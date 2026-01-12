package ru.resodostudio.muzyakich.ui.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.largeContainerSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberNextButtonState
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberPreviousButtonState
import ru.resodostudio.muzyakich.core.designsystem.component.AnimatedIcon
import ru.resodostudio.muzyakich.core.designsystem.component.MuzTonalIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Pause
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlayArrow
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.SkipNext
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.SkipPrevious
import ru.resodostudio.muzyakich.core.locales.R as localesR

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
fun PlayerControlButtonGroup(
    player: Player,
    modifier: Modifier = Modifier,
) {
    val previousButtonState = rememberPreviousButtonState(player)
    val playPauseButtonState = rememberPlayPauseButtonState(player)
    val nextButtonState = rememberNextButtonState(player)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MuzTonalIconButton(
            onClick = previousButtonState::onClick,
            containerSize = largeContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow),
            iconSize = IconButtonDefaults.largeIconSize,
            icon = MuzIcons.Rounded.SkipPrevious,
            contentDescription = stringResource(localesR.string.skip_previous),
        )
        FilledIconButton(
            onClick = playPauseButtonState::onClick,
            shapes = IconButtonDefaults.shapes(),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(largeContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
        ) {
            AnimatedIcon(
                icon = if (playPauseButtonState.showPlay) MuzIcons.Rounded.PlayArrow else MuzIcons.Rounded.Pause,
                contentDescription = if (playPauseButtonState.showPlay) {
                    stringResource(localesR.string.play_audio)
                } else {
                    stringResource(localesR.string.pause_audio)
                },
                label = "PlayPauseIconAnimation",
                iconSize = 32.dp,
            )
        }
        MuzTonalIconButton(
            onClick = nextButtonState::onClick,
            enabled = nextButtonState.isEnabled,
            containerSize = largeContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow),
            icon = MuzIcons.Rounded.SkipNext,
            iconSize = IconButtonDefaults.largeIconSize,
            contentDescription = stringResource(localesR.string.skip_next),
        )
    }
}