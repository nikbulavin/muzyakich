package ru.resodostudio.muzyakich.feature.song.list.impl

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorPosition
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.PopupProperties
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Star
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowRight
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Check
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.FilterList
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Sort
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.SortByAlpha
import ru.resodostudio.muzyakich.core.model.data.FilterConfig
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
)
@Composable
internal fun FilterDropdownMenu(
    filterConfig: FilterConfig,
    modifier: Modifier = Modifier,
    onToggleFilterFavorites: (Boolean) -> Unit = {},
    onSortByUpdate: (SortBy) -> Unit = {},
    onSortOrderUpdate: (SortOrder) -> Unit = {},
) {
    val hapticFeedback = LocalHapticFeedback.current

    val groupInteractionSource = remember { MutableInteractionSource() }
    var expanded by remember { mutableStateOf(false) }

    Box {
        MuzIconButton(
            onClick = { expanded = true },
            icon = MuzIcons.Rounded.FilterList,
            contentDescription = stringResource(localesR.string.core_locales_open_filter_menu),
            modifier = modifier,
        )
        DropdownMenuPopup(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuGroup(
                shapes = MenuDefaults.groupShape(0, 1),
                interactionSource = groupInteractionSource,
                containerColor = MenuDefaults.groupVibrantContainerColor,
            ) {
                val colors = MenuDefaults.selectableItemVibrantColors()
                DropdownMenuItem(
                    checked = filterConfig.shouldFilterFavorites,
                    onCheckedChange = { checked ->
                        hapticFeedback.performHapticFeedback(
                            if (checked) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff
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
                Box {
                    val itemInteractionSource = remember { MutableInteractionSource() }
                    val itemHovered by itemInteractionSource.collectIsHoveredAsState()
                    var itemChecked by remember { mutableStateOf(false) }
                    DropdownMenuItem(
                        selected = false,
                        interactionSource = itemInteractionSource,
                        text = { Text(stringResource(localesR.string.core_locales_sort_by)) },
                        shapes = MenuDefaults.itemShape(1, 3),
                        leadingIcon = {
                            Icon(
                                imageVector = MuzIcons.Rounded.Sort,
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
                        supportingText = {
                            Text(
                                text = if (filterConfig.sortBy == SortBy.TITLE) {
                                    stringResource(localesR.string.core_locales_title)
                                } else {
                                    stringResource(localesR.string.core_locales_artist)
                                },
                            )
                        },
                    )

                    DropdownMenuPopup(
                        popupPositionProvider = MenuDefaults.rememberDropdownMenuPopupPositionProvider(
                            MenuAnchorPosition.End
                        ),
                        expanded = itemChecked || itemHovered,
                        onDismissRequest = { itemChecked = false },
                        properties = PopupProperties(focusable = false),
                    ) {

                    }
                }
                Box {
                    val itemInteractionSource = remember { MutableInteractionSource() }
                    val itemHovered by itemInteractionSource.collectIsHoveredAsState()
                    var itemChecked by remember { mutableStateOf(false) }
                    DropdownMenuItem(
                        selected = false,
                        interactionSource = itemInteractionSource,
                        text = { Text(stringResource(localesR.string.core_locales_sort_order)) },
                        shapes = MenuDefaults.itemShape(2, 3),
                        leadingIcon = {
                            Icon(
                                imageVector = MuzIcons.Rounded.SortByAlpha,
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
                        supportingText = {
                            Text(
                                text = if (filterConfig.sortOrder == SortOrder.DESCENDING) {
                                    stringResource(localesR.string.core_locales_sort_order_descending)
                                } else {
                                    stringResource(localesR.string.core_locales_sort_order_ascending)
                                },
                            )
                        },
                    )

                    DropdownMenuPopup(
                        popupPositionProvider = MenuDefaults.rememberDropdownMenuPopupPositionProvider(
                            MenuAnchorPosition.End
                        ),
                        expanded = itemChecked || itemHovered,
                        onDismissRequest = { itemChecked = false },
                        properties = PopupProperties(focusable = false),
                    ) {

                    }
                }
            }
        }
    }
}