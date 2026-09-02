package ru.resodostudio.muzyakich.feature.player.impl.component

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.smallContainerSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.media3.cast.MediaRouteButton
import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberRepeatButtonState
import androidx.media3.ui.compose.state.rememberShuffleButtonState
import ru.resodostudio.muzyakich.core.designsystem.component.MuzOutlinedIconToggleButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.QueueMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Repeat
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.RepeatOne
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Shuffle
import ru.resodostudio.muzyakich.core.locales.R as localesR

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlaybackButtonGroup(
    player: Player?,
    modifier: Modifier = Modifier,
    queueOpened: Boolean = false,
    onQueueClick: (Boolean) -> Unit = {},
) {
    val shuffleButtonState = rememberShuffleButtonState(player)
    val repeatButtonState = rememberRepeatButtonState(
        player = player,
        toggleModeSequence = listOf(REPEAT_MODE_OFF, REPEAT_MODE_ALL, REPEAT_MODE_ONE),
    )

    val buttonContainerSize = smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)
    val buttonShape = IconButtonDefaults.smallRoundShape
    ButtonGroup(
        overflowIndicator = {},
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        customItem(
            buttonGroupContent = {
                val interactionSource = remember { MutableInteractionSource() }
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                    MediaRouteButton(
                        modifier = Modifier
                            .weight(1f)
                            .animateWidth(interactionSource)
                            .clip(buttonShape)
                            .border(
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                shape = buttonShape,
                            )
                            .sizeIn(
                                minWidth = buttonContainerSize.width,
                                maxHeight = buttonContainerSize.height,
                            )
                            .align(Alignment.CenterVertically),
                    )
                }
            },
            menuContent = {},
        )
        customItem(
            buttonGroupContent = {
                val interactionSource = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .animateWidth(interactionSource),
                ) {
                    MuzOutlinedIconToggleButton(
                        enabled = shuffleButtonState.isEnabled,
                        size = buttonContainerSize,
                        checked = shuffleButtonState.shuffleOn,
                        onCheckedChange = { shuffleButtonState.onClick() },
                        shape = buttonShape,
                        icon = MuzIcons.Rounded.Shuffle,
                        contentDescriptionRes = localesR.string.core_locales_shuffle,
                        interactionSource = interactionSource,
                        isInsideButtonGroup = true,
                    )
                }
            },
            menuContent = {},
        )
        customItem(
            buttonGroupContent = {
                val interactionSource = remember { MutableInteractionSource() }
                val icon = repeatModeIcon(repeatButtonState.repeatModeState)
                val contentDescriptionRes = repeatModeContentDescription(repeatButtonState.repeatModeState)
                val hapticFeedback = LocalHapticFeedback.current

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .animateWidth(interactionSource),
                ) {
                    MuzOutlinedIconToggleButton(
                        enabled = repeatButtonState.isEnabled,
                        size = buttonContainerSize,
                        checked = repeatButtonState.repeatModeState != REPEAT_MODE_OFF,
                        icon = icon,
                        contentDescriptionRes = contentDescriptionRes,
                        onCustomCheckedChange = {
                            when (repeatButtonState.repeatModeState) {
                                REPEAT_MODE_OFF -> hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                REPEAT_MODE_ALL -> hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                REPEAT_MODE_ONE -> hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOff)
                            }
                            repeatButtonState.onClick()
                        },
                        shape = buttonShape,
                        interactionSource = interactionSource,
                        isInsideButtonGroup = true,
                    )
                }
            },
            menuContent = {},
        )
        customItem(
            buttonGroupContent = {
                val interactionSource = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .animateWidth(interactionSource),
                ) {
                    MuzOutlinedIconToggleButton(
                        size = buttonContainerSize,
                        checked = queueOpened,
                        icon = MuzIcons.Rounded.QueueMusic,
                        contentDescriptionRes = localesR.string.core_locales_queue,
                        onCheckedChange = onQueueClick,
                        shape = buttonShape,
                        interactionSource = interactionSource,
                        isInsideButtonGroup = true,
                    )
                }
            },
            menuContent = {},
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
        REPEAT_MODE_OFF -> localesR.string.core_locales_enable_repeat_mode_all
        REPEAT_MODE_ALL -> localesR.string.core_locales_enable_repeat_mode_one
        else -> localesR.string.core_locales_disable_repeat_mode
    }
}
