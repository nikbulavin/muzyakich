package ru.resodostudio.muzyakich.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.FormatPaint
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowBack
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.FormatPaint
import ru.resodostudio.muzyakich.core.designsystem.theme.supportsDynamicTheming
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
        onDynamicColorPreferenceUpdate = viewModel::updateDynamicColorPreference,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    settingsUiState: SettingsUiState,
    onBackClick: () -> Unit,
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
                        useDynamicColor = settingsUiState.useDynamicColor,
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
    useDynamicColor: Boolean,
    onDynamicColorPreferenceUpdate: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
    ) {
        SectionTitle(
            titleRes = localesR.string.appearance,
            modifier = Modifier.padding(start = 16.dp, bottom = 10.dp),
        )
        if (supportsDynamicTheming()) {
            MuzToggableListItem(
                shapes = ListItemDefaults.segmentedShapes(0, 1),
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