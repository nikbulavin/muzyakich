package ru.resodostudio.muzyakich.feature.song.list.impl

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorPosition
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemShapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.PopupProperties
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Star
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowDownwardAlt
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowRight
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowUpwardAlt
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Check
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.FilterList
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Sort
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.SortByAlpha
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Title
import ru.resodostudio.muzyakich.core.model.FilterConfig
import ru.resodostudio.muzyakich.core.model.SortBy
import ru.resodostudio.muzyakich.core.model.SortOrder
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun FilterDropdownMenu(
    filterConfig: FilterConfig,
    modifier: Modifier = Modifier,
    onToggleFilterFavorites: (Boolean) -> Unit = {},
    onSortByUpdate: (SortBy) -> Unit = {},
    onSortOrderUpdate: (SortOrder) -> Unit = {},
) {
    val hapticFeedback = LocalHapticFeedback.current
    var expanded by remember { mutableStateOf(false) }

    Box {
        MuzIconButton(
            onClick = { expanded = true },
            icon = MuzIcons.Rounded.FilterList,
            contentDescription = stringResource(localesR.string.core_locales_open_filter_menu),
            modifier = modifier,
        )
        val colors = MenuDefaults.selectableItemVibrantColors()
        DropdownMenuPopup(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuGroup(
                shapes = MenuDefaults.groupShape(0, 2),
                containerColor = MenuDefaults.groupVibrantContainerColor,
            ) {
                DropdownMenuItem(
                    checked = filterConfig.shouldFilterFavorites,
                    onCheckedChange = { checked ->
                        hapticFeedback.performHapticFeedback(
                            if (checked) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff,
                        )
                        onToggleFilterFavorites(checked)
                    },
                    text = { Text(stringResource(localesR.string.core_locales_favorites)) },
                    shapes = MenuDefaults.itemShape(0, 3),
                    leadingIcon = {
                        Icon(
                            imageVector = MuzIcons.Filled.Star,
                            modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                            contentDescription = null,
                        )
                    },
                    checkedLeadingIcon = {
                        Icon(
                            imageVector = MuzIcons.Rounded.Check,
                            modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                            contentDescription = null,
                        )
                    },
                    colors = colors,
                )
            }
            Spacer(Modifier.height(MenuDefaults.GroupSpacing))
            DropdownMenuGroup(
                shapes = MenuDefaults.groupShape(1, 2),
                containerColor = MenuDefaults.groupVibrantContainerColor,
            ) {
                FilterSubMenu(
                    label = stringResource(localesR.string.core_locales_sort_by),
                    leadingIcon = MuzIcons.Rounded.Sort,
                    supportingText = if (filterConfig.sortBy == SortBy.TITLE) {
                        stringResource(localesR.string.core_locales_title)
                    } else {
                        stringResource(localesR.string.core_locales_artist)
                    },
                    options = listOf(
                        SubMenuOption(
                            value = SortBy.ARTIST,
                            label = stringResource(localesR.string.core_locales_artist),
                            icon = MuzIcons.Filled.Artist,
                        ),
                        SubMenuOption(
                            value = SortBy.TITLE,
                            label = stringResource(localesR.string.core_locales_title),
                            icon = MuzIcons.Rounded.Title,
                        ),
                    ),
                    selectedValue = filterConfig.sortBy,
                    onOptionSelected = onSortByUpdate,
                    shapes = MenuDefaults.itemShape(1, 3),
                )
                FilterSubMenu(
                    label = stringResource(localesR.string.core_locales_sort_order),
                    leadingIcon = MuzIcons.Rounded.SortByAlpha,
                    supportingText = if (filterConfig.sortOrder == SortOrder.DESCENDING) {
                        stringResource(localesR.string.core_locales_sort_order_descending)
                    } else {
                        stringResource(localesR.string.core_locales_sort_order_ascending)
                    },
                    options = listOf(
                        SubMenuOption(
                            value = SortOrder.ASCENDING,
                            label = stringResource(localesR.string.core_locales_sort_order_ascending),
                            icon = MuzIcons.Rounded.ArrowUpwardAlt,
                        ),
                        SubMenuOption(
                            value = SortOrder.DESCENDING,
                            label = stringResource(localesR.string.core_locales_sort_order_descending),
                            icon = MuzIcons.Rounded.ArrowDownwardAlt,
                        ),
                    ),
                    selectedValue = filterConfig.sortOrder,
                    onOptionSelected = onSortOrderUpdate,
                    shapes = MenuDefaults.itemShape(2, 3),
                )
            }
        }
    }
}

private data class SubMenuOption<T>(
    val value: T,
    val label: String,
    val icon: ImageVector,
)

@Composable
private fun <T> FilterSubMenu(
    label: String,
    leadingIcon: ImageVector,
    supportingText: String,
    options: List<SubMenuOption<T>>,
    selectedValue: T,
    onOptionSelected: (T) -> Unit,
    shapes: MenuItemShapes,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val itemInteractionSource = remember { MutableInteractionSource() }
    val itemHovered by itemInteractionSource.collectIsHoveredAsState()
    var itemChecked by remember { mutableStateOf(false) }
    val colors = MenuDefaults.selectableItemVibrantColors()

    Box {
        DropdownMenuItem(
            selected = false,
            interactionSource = itemInteractionSource,
            text = { Text(label) },
            shapes = shapes,
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                    contentDescription = null,
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = MuzIcons.Rounded.ArrowRight,
                    modifier = Modifier.size(MenuDefaults.TrailingIconSize),
                    contentDescription = null,
                )
            },
            onClick = { itemChecked = !itemChecked },
            colors = colors,
            supportingText = { Text(supportingText) },
        )

        DropdownMenuPopup(
            popupPositionProvider = MenuDefaults.rememberDropdownMenuPopupPositionProvider(
                MenuAnchorPosition.End,
            ),
            expanded = itemChecked || itemHovered,
            onDismissRequest = { itemChecked = false },
            properties = PopupProperties(focusable = false),
        ) {
            DropdownMenuGroup(
                shapes = MenuDefaults.groupShape(0, 1),
                interactionSource = itemInteractionSource,
                containerColor = MenuDefaults.groupVibrantContainerColor,
            ) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        selected = selectedValue == option.value,
                        onClick = {
                            if (selectedValue != option.value) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                            }
                            onOptionSelected(option.value)
                        },
                        text = { Text(option.label) },
                        shapes = MenuDefaults.itemShape(index, options.size),
                        leadingIcon = {
                            Icon(
                                imageVector = option.icon,
                                modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                                contentDescription = null,
                            )
                        },
                        selectedLeadingIcon = {
                            Icon(
                                imageVector = MuzIcons.Rounded.Check,
                                modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                                contentDescription = null,
                            )
                        },
                        colors = colors,
                    )
                }
            }
        }
    }
}
