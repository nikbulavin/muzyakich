package ru.resodostudio.muzyakich.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.smallContainerSize
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MuzOutlinedIconToggleButton(
    checked: Boolean,
    icon: ImageVector,
    @StringRes contentDescriptionRes: Int,
    modifier: Modifier = Modifier,
    tooltipPosition: TooltipAnchorPosition = TooltipAnchorPosition.Above,
    enabled: Boolean = true,
    shape: Shape? = null,
    onCheckedChange: (Boolean) -> Unit = {},
    onCustomCheckedChange: ((Boolean) -> Unit)? = null,
    size: DpSize = smallContainerSize(),
) {
    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = tooltipPosition,
        ),
        tooltip = { PlainTooltip { Text(stringResource(contentDescriptionRes)) } },
        state = rememberTooltipState(),
    ) {
        val hapticFeedback = LocalHapticFeedback.current
        OutlinedIconToggleButton(
            checked = checked,
            onCheckedChange = {
                if (onCustomCheckedChange != null) {
                    onCustomCheckedChange(it)
                } else {
                    hapticFeedback.performHapticFeedback(
                        if (!checked) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff
                    )
                    onCheckedChange(!checked)
                }
            },
            shapes = IconButtonDefaults.toggleableShapes(shape),
            colors = IconButtonDefaults.outlinedIconToggleButtonVibrantColors(),
            border = IconButtonDefaults.outlinedIconToggleButtonVibrantBorder(enabled, checked),
            modifier = Modifier.size(size),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(contentDescriptionRes),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MuzIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tooltipPosition: TooltipAnchorPosition = TooltipAnchorPosition.Above,
    colors: IconButtonColors = IconButtonDefaults.iconButtonVibrantColors(),
) {
    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = tooltipPosition,
        ),
        tooltip = { PlainTooltip { Text(contentDescription) } },
        state = rememberTooltipState(),
    ) {
        IconButton(
            onClick = onClick,
            shapes = IconButtonDefaults.shapes(),
            colors = colors,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
            )
        }
    }
}