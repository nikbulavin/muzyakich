package ru.resodostudio.muzyakich.feature.song.list.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.TonalToggleButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.component.AnimatedIcon
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Star
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowDownwardAlt
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowUpwardAlt
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
internal fun FilterBottomSheet(
    filterConfig: FilterConfig,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onToggleFilterFavorites: (Boolean) -> Unit = {},
    onSortByUpdate: (SortBy) -> Unit = {},
    onSortOrderUpdate: (SortOrder) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val hapticFeedback = LocalHapticFeedback.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
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
                text = stringResource(localesR.string.core_locales_filters),
                style = MaterialTheme.typography.titleMedium,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp),
            ) {
                val selected = filterConfig.shouldFilterFavorites
                FilterChip(
                    selected = selected,
                    onClick = {
                        hapticFeedback.performHapticFeedback(
                            if (selected) HapticFeedbackType.ToggleOff else HapticFeedbackType.ToggleOn
                        )
                        onToggleFilterFavorites(!selected)
                    },
                    label = { Text(stringResource(localesR.string.core_locales_favorites)) },
                    leadingIcon = {
                        AnimatedIcon(
                            icon = if (selected) MuzIcons.Rounded.Check else MuzIcons.Filled.Star,
                            iconSize = FilterChipDefaults.IconSize,
                        )
                    },
                )
            }

            Text(
                text = stringResource(localesR.string.core_locales_sort_by),
                style = MaterialTheme.typography.titleMedium,
            )
            val sortByOptions = listOf(
                stringResource(localesR.string.core_locales_artist),
                stringResource(localesR.string.core_locales_title),
            )
            val sortByIcons = listOf(MuzIcons.Filled.Artist, MuzIcons.Rounded.Title)

            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
            ) {
                sortByOptions.forEachIndexed { index, label ->
                    val checked = filterConfig.sortBy.ordinal == index
                    TonalToggleButton(
                        checked = checked,
                        onCheckedChange = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                            onSortByUpdate(SortBy.entries[index])
                        },
                        modifier = Modifier
                            .semantics { role = Role.RadioButton }
                            .weight(1f),
                        shapes = when (index) {
                            0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                            sortByOptions.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                            else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                        },
                        contentPadding = ButtonDefaults.contentPaddingFor(
                            buttonHeight = ToggleButtonDefaults.MinHeight,
                            hasStartIcon = true,
                        ),
                    ) {
                        AnimatedIcon(
                            icon = if (checked) MuzIcons.Rounded.Check else sortByIcons[index],
                            iconSize = ToggleButtonDefaults.IconSize,
                        )
                        Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
                        Text(
                            text = label,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            Text(
                text = stringResource(localesR.string.core_locales_sort_order),
                style = MaterialTheme.typography.titleMedium,
            )
            val sortOrderOptions = listOf(
                stringResource(localesR.string.core_locales_sort_order_ascending),
                stringResource(localesR.string.core_locales_sort_order_descending),
            )
            val sortOrderIcons = listOf(
                MuzIcons.Rounded.ArrowUpwardAlt,
                MuzIcons.Rounded.ArrowDownwardAlt,
            )

            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
            ) {
                sortOrderOptions.forEachIndexed { index, label ->
                    val checked = filterConfig.sortOrder.ordinal == index
                    TonalToggleButton(
                        checked = checked,
                        onCheckedChange = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                            onSortOrderUpdate(SortOrder.entries[index])
                        },
                        modifier = Modifier
                            .semantics { role = Role.RadioButton }
                            .weight(1f),
                        shapes = when (index) {
                            0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                            sortByOptions.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                            else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                        },
                        contentPadding = ButtonDefaults.contentPaddingFor(
                            buttonHeight = ToggleButtonDefaults.MinHeight,
                            hasStartIcon = true,
                        ),
                    ) {
                        AnimatedIcon(
                            icon = if (checked) MuzIcons.Rounded.Check else sortOrderIcons[index],
                            iconSize = ToggleButtonDefaults.IconSize,
                        )
                        Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
                        Text(
                            text = label,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}