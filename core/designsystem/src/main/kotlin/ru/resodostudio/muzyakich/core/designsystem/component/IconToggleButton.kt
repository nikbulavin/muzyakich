package ru.resodostudio.muzyakich.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MuzOutlinedIconToggleButton(
    checked: Boolean,
    icon: ImageVector,
    @StringRes contentDescriptionRes: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape? = null,
    onCheckedChange: (Boolean) -> Unit = {},
    onCustomCheckedChange: ((Boolean) -> Unit)? = null,
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
        modifier = modifier,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(contentDescriptionRes),
        )
    }
}