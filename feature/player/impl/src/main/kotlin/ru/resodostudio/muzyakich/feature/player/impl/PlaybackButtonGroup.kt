package ru.resodostudio.muzyakich.feature.player.impl

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.smallContainerSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun PlaybackButtonGroup(
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
            MediaRouteButton(
                modifier = Modifier
                    .size(smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide))
                    .border(
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        shape = IconButtonDefaults.smallSquareShape,
                    )
                    .clip(IconButtonDefaults.smallSquareShape),
            )
        }
        MuzOutlinedIconToggleButton(
            enabled = shuffleButtonState.isEnabled,
            size = smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide),
            checked = shuffleButtonState.shuffleOn,
            onCheckedChange = { shuffleButtonState.onClick() },
            shape = IconButtonDefaults.smallSquareShape,
            icon = MuzIcons.Rounded.Shuffle,
            contentDescriptionRes = localesR.string.shuffle,
        )
        val icon = repeatModeIcon(repeatButtonState.repeatModeState)
        val contentDescriptionRes = repeatModeContentDescription(repeatButtonState.repeatModeState)
        val hapticFeedback = LocalHapticFeedback.current
        MuzOutlinedIconToggleButton(
            enabled = repeatButtonState.isEnabled,
            size = smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide),
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
            shape = IconButtonDefaults.smallSquareShape,
        )
        MuzOutlinedIconToggleButton(
            size = smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide),
            checked = queueOpened,
            icon = MuzIcons.Rounded.QueueMusic,
            contentDescriptionRes = localesR.string.queue,
            onCheckedChange = onQueueClick,
            shape = IconButtonDefaults.smallSquareShape,
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