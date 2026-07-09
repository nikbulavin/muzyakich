package ru.resodostudio.cashsense.core.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlayArrow
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Shuffle
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun PlayShuffleButtonGroup(
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonSize: Dp = ButtonDefaults.MinHeight,
    enabled: Boolean = true,
) {
    val buttonContentPadding = ButtonDefaults.contentPaddingFor(
        buttonHeight = buttonSize,
        hasStartIcon = true,
    )
    ButtonGroup(
        modifier = modifier,
        overflowIndicator = {},
    ) {
        customItem(
            buttonGroupContent = {
                val interactionSource = remember { MutableInteractionSource() }
                Button(
                    shapes = ButtonDefaults.shapes(),
                    onClick = onPlayClick,
                    modifier = Modifier
                        .heightIn(buttonSize)
                        .weight(1f)
                        .animateWidth(
                            interactionSource = interactionSource,
                            compressionLimit = buttonContentPadding,
                        ),
                    contentPadding = buttonContentPadding,
                    interactionSource = interactionSource,
                    enabled = enabled,
                ) {
                    Icon(
                        imageVector = MuzIcons.Rounded.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.iconSizeFor(buttonSize)),
                    )
                    Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(buttonSize)))
                    Text(
                        text = stringResource(localesR.string.core_locales_play_audio),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = ButtonDefaults.textStyleFor(buttonSize),
                    )
                }
            },
            menuContent = {},
        )
        customItem(
            buttonGroupContent = {
                val interactionSource = remember { MutableInteractionSource() }
                OutlinedButton(
                    shapes = ButtonDefaults.shapes(),
                    onClick = onShuffleClick,
                    modifier = Modifier
                        .heightIn(buttonSize)
                        .weight(1f)
                        .animateWidth(
                            interactionSource = interactionSource,
                            compressionLimit = buttonContentPadding,
                        ),
                    contentPadding = buttonContentPadding,
                    interactionSource = interactionSource,
                    enabled = enabled,
                ) {
                    Icon(
                        imageVector = MuzIcons.Rounded.Shuffle,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.iconSizeFor(buttonSize)),
                    )
                    Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(buttonSize)))
                    Text(
                        text = stringResource(localesR.string.core_locales_shuffle),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = ButtonDefaults.textStyleFor(buttonSize),
                    )
                }
            },
            menuContent = {},
        )
    }
}