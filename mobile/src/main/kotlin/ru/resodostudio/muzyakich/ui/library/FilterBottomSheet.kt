package ru.resodostudio.muzyakich.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Star
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Check
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    shouldFilterFavorites: Boolean = false,
    onToggleFilterFavorites: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Filters",
                style = MaterialTheme.typography.titleMedium,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val hapticFeedback = LocalHapticFeedback.current
                FilterChip(
                    selected = shouldFilterFavorites,
                    onClick = {
                        hapticFeedback.performHapticFeedback(
                            if (shouldFilterFavorites) HapticFeedbackType.ToggleOff else HapticFeedbackType.ToggleOn
                        )
                        onToggleFilterFavorites()
                    },
                    label = {
                        Text(
                            text = stringResource(localesR.string.favorites),
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = if (shouldFilterFavorites) MuzIcons.Rounded.Check else MuzIcons.Filled.Star,
                            contentDescription = null,
                            modifier = modifier.size(FilterChipDefaults.IconSize),
                        )
                    },
                )
            }
        }
    }
}