package ru.resodostudio.muzyakich.core.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Check
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Close

@Composable
fun MuzSwitch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: ((Boolean) -> Unit)? = null,
) {
    val hapticFeedback = LocalHapticFeedback.current
    Switch(
        checked = checked,
        onCheckedChange = if (onCheckedChange != null) { isChecked ->
            hapticFeedback.performHapticFeedback(
                if (isChecked) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff
            )
            onCheckedChange(isChecked)
        } else null,
        thumbContent = {
            AnimatedIcon(
                icon = if (checked) MuzIcons.Rounded.Check else MuzIcons.Rounded.Close,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        },
        modifier = modifier,
    )
}