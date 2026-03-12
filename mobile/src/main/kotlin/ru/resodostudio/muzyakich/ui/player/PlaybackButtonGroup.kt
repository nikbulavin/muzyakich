package ru.resodostudio.muzyakich.ui.player

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.cast.MediaRouteButton
import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberRepeatButtonState
import androidx.media3.ui.compose.state.rememberShuffleButtonState
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconToggleButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.QueueMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Repeat
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.RepeatOne
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Shuffle
import ru.resodostudio.muzyakich.core.model.data.RepeatMode.REPEAT_ALL
import ru.resodostudio.muzyakich.core.model.data.RepeatMode.REPEAT_OFF
import ru.resodostudio.muzyakich.core.model.data.RepeatMode.REPEAT_ONE
import ru.resodostudio.muzyakich.core.locales.R as localesR

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun PlaybackButtonGroup(
    player: Player,
    modifier: Modifier = Modifier,
    queueOpened: Boolean = false,
    onQueueClick: (Boolean) -> Unit = {},
) {
    val shuffleButtonState = rememberShuffleButtonState(player)
    val repeatButtonState = rememberRepeatButtonState(
        player = player,
        toggleModeSequence = listOf(REPEAT_MODE_OFF, REPEAT_MODE_ALL, REPEAT_MODE_ONE),
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
            MediaRouteButton()
        }
        MuzIconToggleButton(
            enabled = shuffleButtonState.isEnabled,
            checked = shuffleButtonState.shuffleOn,
            onCheckedChange = { shuffleButtonState.onClick() },
            icon = MuzIcons.Rounded.Shuffle,
            contentDescription = stringResource(localesR.string.shuffle),
        )
        val contentDescription = stringResource(repeatModeContentDescription(repeatButtonState.repeatModeState))
        TooltipBox(
            modifier = modifier,
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                positioning = TooltipAnchorPosition.Above,
            ),
            tooltip = { PlainTooltip { Text(contentDescription) } },
            state = rememberTooltipState(),
        ) {
            val hapticFeedback = LocalHapticFeedback.current
            IconToggleButton(
                enabled = repeatButtonState.isEnabled,
                checked = repeatButtonState.repeatModeState != REPEAT_MODE_OFF,
                onCheckedChange = {
                    when (repeatButtonState.repeatModeState) {
                        REPEAT_MODE_OFF -> {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                            REPEAT_ALL
                        }

                        REPEAT_MODE_ALL -> {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                            REPEAT_ONE
                        }

                        REPEAT_MODE_ONE -> {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOff)
                            REPEAT_OFF
                        }
                    }
                    repeatButtonState.onClick()
                },
                shapes = IconButtonDefaults.toggleableShapes(),
                colors = IconButtonDefaults.iconToggleButtonVibrantColors(),
            ) {
                Icon(
                    imageVector = repeatModeIcon(repeatButtonState.repeatModeState),
                    contentDescription = contentDescription,
                )
            }
        }
        MuzIconToggleButton(
            checked = queueOpened,
            icon = MuzIcons.Rounded.QueueMusic,
            contentDescription = stringResource(localesR.string.queue),
            onCheckedChange = onQueueClick,
        )
    }
}

private fun repeatModeIcon(repeatMode: @Player.RepeatMode Int): ImageVector {
    return if (repeatMode == REPEAT_MODE_ONE) {
        MuzIcons.Rounded.RepeatOne
    } else {
        MuzIcons.Rounded.Repeat
    }
}

@StringRes
private fun repeatModeContentDescription(repeatMode: @Player.RepeatMode Int): Int {
    return when (repeatMode) {
        REPEAT_MODE_OFF -> localesR.string.enable_repeat_mode_all
        REPEAT_MODE_ALL -> localesR.string.enable_repeat_mode_one
        else -> localesR.string.disable_repeat_mode
    }
}