package ru.resodostudio.muzyakich.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.smallContainerSize
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.OutlinedIconButton
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MuzIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tooltipPosition: TooltipAnchorPosition = TooltipAnchorPosition.Above,
    colors: IconButtonColors = IconButtonDefaults.iconButtonVibrantColors(),
    enabled: Boolean = true,
    containerSize: DpSize = smallContainerSize(),
    iconSize: Dp = IconButtonDefaults.smallIconSize,
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
            enabled = enabled,
            modifier = Modifier.size(containerSize),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MuzFilledTonalIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tooltipPosition: TooltipAnchorPosition = TooltipAnchorPosition.Above,
    colors: IconButtonColors = IconButtonDefaults.filledTonalIconButtonColors(),
    containerSize: DpSize = smallContainerSize(),
    iconSize: Dp = IconButtonDefaults.smallIconSize,
    enabled: Boolean = true,
) {
    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = tooltipPosition,
        ),
        tooltip = { PlainTooltip { Text(contentDescription) } },
        state = rememberTooltipState(),
    ) {
        FilledTonalIconButton(
            onClick = onClick,
            shapes = IconButtonDefaults.shapes(),
            colors = colors,
            modifier = Modifier.size(containerSize),
            enabled = enabled,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MuzFilledIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tooltipPosition: TooltipAnchorPosition = TooltipAnchorPosition.Above,
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(),
    containerSize: DpSize = smallContainerSize(),
    iconSize: Dp = IconButtonDefaults.smallIconSize,
    enabled: Boolean = true,
) {
    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = tooltipPosition,
        ),
        tooltip = { PlainTooltip { Text(contentDescription) } },
        state = rememberTooltipState(),
    ) {
        FilledIconButton(
            onClick = onClick,
            shapes = IconButtonDefaults.shapes(),
            colors = colors,
            modifier = Modifier.size(containerSize),
            enabled = enabled,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MuzFilledIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tooltipPosition: TooltipAnchorPosition = TooltipAnchorPosition.Above,
    colors: IconToggleButtonColors = IconButtonDefaults.filledIconToggleButtonColors(),
    containerSize: DpSize = smallContainerSize(),
    iconSize: Dp = IconButtonDefaults.smallIconSize,
) {
    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = tooltipPosition,
        ),
        tooltip = { PlainTooltip { Text(contentDescription) } },
        state = rememberTooltipState(),
    ) {
        val hapticFeedback = LocalHapticFeedback.current
        FilledIconToggleButton(
            checked = checked,
            onCheckedChange = {
                hapticFeedback.performHapticFeedback(
                    if (!checked) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff
                )
                onCheckedChange(!checked)
            },
            shapes = IconButtonDefaults.toggleableShapes(),
            colors = colors,
            modifier = Modifier.size(containerSize),
        ) {
            AnimatedIcon(
                icon = icon,
                contentDescription = contentDescription,
                iconSize = iconSize,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MuzOutlinedIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tooltipPosition: TooltipAnchorPosition = TooltipAnchorPosition.Above,
    containerSize: DpSize = smallContainerSize(),
    iconSize: Dp = IconButtonDefaults.smallIconSize,
    enabled: Boolean = true,
) {
    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = tooltipPosition,
        ),
        tooltip = { PlainTooltip { Text(contentDescription) } },
        state = rememberTooltipState(),
    ) {
        OutlinedIconButton(
            onClick = onClick,
            shapes = IconButtonDefaults.shapes(),
            colors = IconButtonDefaults.outlinedIconButtonVibrantColors(),
            enabled = enabled,
            border = IconButtonDefaults.outlinedIconButtonVibrantBorder(true),
            modifier = Modifier.size(containerSize),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MuzFilledTonalIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tooltipPosition: TooltipAnchorPosition = TooltipAnchorPosition.Above,
    colors: IconToggleButtonColors = IconButtonDefaults.filledTonalIconToggleButtonColors(),
    containerSize: DpSize = smallContainerSize(),
    iconSize: Dp = IconButtonDefaults.smallIconSize,
) {
    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = tooltipPosition,
        ),
        tooltip = { PlainTooltip { Text(contentDescription) } },
        state = rememberTooltipState(),
    ) {
        val hapticFeedback = LocalHapticFeedback.current
        FilledTonalIconToggleButton(
            checked = checked,
            onCheckedChange = {
                hapticFeedback.performHapticFeedback(
                    if (!checked) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff
                )
                onCheckedChange(!checked)
            },
            shapes = IconButtonDefaults.toggleableShapes(),
            colors = colors,
            modifier = Modifier.size(containerSize),
        ) {
            AnimatedIcon(
                icon = icon,
                contentDescription = contentDescription,
                iconSize = iconSize,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MuzIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tooltipPosition: TooltipAnchorPosition = TooltipAnchorPosition.Above,
    colors: IconToggleButtonColors = IconButtonDefaults.iconToggleButtonVibrantColors(),
    containerSize: DpSize = smallContainerSize(),
    iconSize: Dp = IconButtonDefaults.smallIconSize,
) {
    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = tooltipPosition,
        ),
        tooltip = { PlainTooltip { Text(contentDescription) } },
        state = rememberTooltipState(),
    ) {
        val hapticFeedback = LocalHapticFeedback.current
        IconToggleButton(
            checked = checked,
            onCheckedChange = {
                hapticFeedback.performHapticFeedback(
                    if (!checked) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff
                )
                onCheckedChange(!checked)
            },
            shapes = IconButtonDefaults.toggleableShapes(),
            colors = colors,
            modifier = Modifier.size(containerSize),
        ) {
            AnimatedIcon(
                icon = icon,
                contentDescription = contentDescription,
                iconSize = iconSize,
            )
        }
    }
}