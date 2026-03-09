package ru.resodostudio.muzyakich.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.resodostudio.muzyakich.core.designsystem.component.AnimatedIcon
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSwitch
import ru.resodostudio.muzyakich.core.designsystem.component.MuzToggableListItem
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.DarkMode
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.FormatPaint
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.LightMode
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Palette
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Android
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowBack
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.DarkMode
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.FormatPaint
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.LightMode
import ru.resodostudio.muzyakich.core.designsystem.theme.supportsDynamicTheming
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig
import ru.resodostudio.muzyakich.ui.component.LoadingState
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    SettingsScreen(
        settingsUiState = settingsUiState,
        onBackClick = onBackClick,
        onDarkThemeConfigUpdate = viewModel::updateDarkThemeConfig,
        onDynamicColorPreferenceUpdate = viewModel::updateDynamicColorPreference,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    settingsUiState: SettingsUiState,
    onBackClick: () -> Unit,
    onDarkThemeConfigUpdate: (DarkThemeConfig) -> Unit,
    onDynamicColorPreferenceUpdate: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(localesR.string.settings)) },
                navigationIcon = {
                    MuzIconButton(
                        onClick = onBackClick,
                        icon = MuzIcons.Rounded.ArrowBack,
                        contentDescription = stringResource(localesR.string.back),
                    )
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            when (settingsUiState) {
                SettingsUiState.Loading -> LoadingState(modifier = Modifier.fillMaxSize())
                is SettingsUiState.Success -> {
                    Appearance(
                        darkThemeConfig = settingsUiState.darkThemeConfig,
                        useDynamicColor = settingsUiState.useDynamicColor,
                        onDarkThemeConfigUpdate = onDarkThemeConfigUpdate,
                        onDynamicColorPreferenceUpdate = onDynamicColorPreferenceUpdate,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun Appearance(
    darkThemeConfig: DarkThemeConfig,
    useDynamicColor: Boolean,
    onDarkThemeConfigUpdate: (DarkThemeConfig) -> Unit,
    onDynamicColorPreferenceUpdate: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
    ) {
        SectionTitle(
            titleRes = localesR.string.appearance,
            modifier = Modifier.padding(start = 16.dp, bottom = 10.dp, top = 16.dp),
        )
        ListItem(
            headlineContent = { Text(stringResource(localesR.string.theme)) },
            leadingContent = {
                Icon(
                    imageVector = MuzIcons.Filled.Palette,
                    contentDescription = null,
                )
            },
            trailingContent = {
                val themeOptions = listOf(
                    stringResource(localesR.string.theme_system_default),
                    stringResource(localesR.string.theme_light),
                    stringResource(localesR.string.theme_dark),
                )
                val uncheckedIcons = listOf(
                    MuzIcons.Rounded.Android,
                    MuzIcons.Rounded.LightMode,
                    MuzIcons.Rounded.DarkMode,
                )
                val checkedIcons = listOf(
                    MuzIcons.Rounded.Android,
                    MuzIcons.Filled.LightMode,
                    MuzIcons.Filled.DarkMode,
                )
                val selectedIndex = darkThemeConfig.ordinal
                val hapticFeedback = LocalHapticFeedback.current

                Row(
                    horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
                ) {
                    themeOptions.forEachIndexed { index, label ->
                        ToggleButton(
                            checked = selectedIndex == index,
                            onCheckedChange = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                onDarkThemeConfigUpdate(DarkThemeConfig.entries[index])
                            },
                            shapes = when (index) {
                                0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                themeOptions.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                            },
                            colors = ToggleButtonDefaults.toggleButtonColors().copy(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            ),
                        ) {
                            AnimatedIcon(
                                icon = if (selectedIndex == index) checkedIcons[index] else uncheckedIcons[index],
                                contentDescription = label,
                                modifier = Modifier.size(ToggleButtonDefaults.IconSize),
                            )
                        }
                    }
                }
            },
            modifier = Modifier.clip(
                if (supportDynamicColor) {
                    ListItemDefaults.segmentedShapes(0, 2).shape
                } else {
                    RoundedCornerShape(16.dp)
                }
            ),
            colors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
            ),
        )
        if (supportDynamicColor) {
            MuzToggableListItem(
                shapes = ListItemDefaults.segmentedShapes(1, 2),
                content = { Text(stringResource(localesR.string.dynamic_color)) },
                leadingContent = {
                    AnimatedIcon(
                        icon = if (useDynamicColor) MuzIcons.Filled.FormatPaint else MuzIcons.Rounded.FormatPaint,
                        contentDescription = null,
                    )
                },
                trailingContent = {
                    MuzSwitch(
                        checked = useDynamicColor,
                        onCheckedChange = null,
                    )
                },
                checked = useDynamicColor,
                onCheckedChange = onDynamicColorPreferenceUpdate,
            )
        }
    }
}

@Composable
private fun SectionTitle(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(titleRes),
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}