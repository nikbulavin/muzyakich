package ru.resodostudio.muzyakich.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Star
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Check
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Title
import ru.resodostudio.muzyakich.core.model.data.FilterConfig
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
)
@Composable
fun FilterBottomSheet(
    filterConfig: FilterConfig,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    shouldFilterFavorites: Boolean = false,
    onToggleFilterFavorites: () -> Unit = {},
    onSortByUpdate: (SortBy) -> Unit = {},
    onSortOrderUpdate: (SortOrder) -> Unit = {},
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
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(localesR.string.filters),
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
                    label = { Text(stringResource(localesR.string.favorites)) },
                    leadingIcon = {
                        Icon(
                            imageVector = if (shouldFilterFavorites) MuzIcons.Rounded.Check else MuzIcons.Filled.Star,
                            contentDescription = null,
                            modifier = modifier.size(FilterChipDefaults.IconSize),
                        )
                    },
                )
            }

            Text(
                text = stringResource(localesR.string.sort_by),
                style = MaterialTheme.typography.titleMedium,
            )
            val sortByOptions = listOf(stringResource(localesR.string.title), stringResource(localesR.string.artist))
            val unCheckedIcons = listOf(MuzIcons.Rounded.Title, MuzIcons.Filled.Artist)

            Row(
                Modifier.padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
            ) {
                sortByOptions.forEachIndexed { index, label ->
                    val selected = filterConfig.sortBy.ordinal == index
                    ToggleButton(
                        checked = selected,
                        onCheckedChange = { onSortByUpdate(SortBy.entries[index]) },
                        modifier = Modifier
                            .semantics { role = Role.RadioButton }
                            .weight(1f),
                        shapes =
                            when (index) {
                                0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                sortByOptions.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                            },
                    ) {
                        Icon(
                            if (selected) MuzIcons.Rounded.Check else unCheckedIcons[index],
                            contentDescription = null,
                        )
                        Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
                        Text(label)
                    }
                }
            }
        }
    }
}