package ru.resodostudio.muzyakich.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledIconToggleButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Add
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.library.impl.model.LibraryTab
import ru.resodostudio.muzyakich.feature.playlist.editor.api.navigateToPlaylistEditor
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationToolbar(
    currentLibraryTab: LibraryTab,
    libraryNavigator: Navigator,
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HorizontalFloatingToolbar(
            expanded = false,
            content = {
                val tabs = LibraryTab.entries
                val labels = tabs.associateWith { stringResource(it.titleRes) }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    tabs.forEach { tab ->
                        val selected = tab == currentLibraryTab
                        MuzFilledIconToggleButton(
                            checked = selected,
                            onCheckedChange = {
                                libraryNavigator.navigateAndClearStack(
                                    tab.navKey
                                )
                            },
                            icon = if (selected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = labels[tab] ?: "",
                            containerSize = IconButtonDefaults.smallContainerSize(
                                IconButtonDefaults.IconButtonWidthOption.Wide
                            ),
                            shapes = IconButtonDefaults.toggleableShapes(
                                checkedShape = CircleShape,
                            ),
                            shouldAnimateIcon = false,
                            shouldVibrateOnToggle = false,
                        )
                    }
                }
            },
            collapsedShadowElevation = 3.dp,
        )
        FloatingToolbarDefaults.StandardFloatingActionButton(
            onClick = dropUnlessResumed { navigator.navigateToPlaylistEditor() },
            modifier = Modifier
                .animateFloatingActionButton(
                    visible = currentLibraryTab == LibraryTab.PLAYLISTS,
                    alignment = Alignment.BottomCenter,
                )
                .size(56.dp),
        ) {
            Icon(
                imageVector = MuzIcons.Rounded.Add,
                contentDescription = stringResource(localesR.string.core_locales_new_playlist),
            )
        }
    }
}