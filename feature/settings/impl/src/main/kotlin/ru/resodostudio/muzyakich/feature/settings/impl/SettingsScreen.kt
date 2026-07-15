package ru.resodostudio.muzyakich.feature.settings.impl

import android.content.Context
import android.content.Intent
import android.media.audiofx.AudioEffect
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.resodostudio.cashsense.core.ui.LoadingState
import ru.resodostudio.muzyakich.core.designsystem.component.AnimatedIcon
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzListItem
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSwitch
import ru.resodostudio.muzyakich.core.designsystem.component.MuzToggableListItem
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.DarkMode
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Feedback
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.FormatPaint
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Gavel
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Info
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.LightMode
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Palette
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Policy
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Russia
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.SouthKorea
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.UnitedStates
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Android
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowBack
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.FormatPaint
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.GraphicEq
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Language
import ru.resodostudio.muzyakich.core.designsystem.theme.supportsDynamicTheming
import ru.resodostudio.muzyakich.core.model.DarkThemeConfig
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun SettingsScreen(
    onBackClick: () -> Unit,
    onLicensesClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    SettingsScreen(
        settingsUiState = settingsUiState,
        onBackClick = onBackClick,
        onLicensesClick = onLicensesClick,
        onDarkThemeConfigUpdate = viewModel::updateDarkThemeConfig,
        onDynamicColorPreferenceUpdate = viewModel::updateDynamicColorPreference,
        onLanguageUpdate = viewModel::setAppLanguage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    settingsUiState: SettingsUiState,
    onBackClick: () -> Unit,
    onLicensesClick: () -> Unit,
    onDarkThemeConfigUpdate: (DarkThemeConfig) -> Unit,
    onDynamicColorPreferenceUpdate: (Boolean) -> Unit,
    onLanguageUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(localesR.string.core_locales_settings)) },
                navigationIcon = {
                    MuzIconButton(
                        onClick = onBackClick,
                        icon = MuzIcons.Rounded.ArrowBack,
                        contentDescription = stringResource(localesR.string.core_locales_back),
                        tooltipPosition = TooltipAnchorPosition.Right,
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(top = innerPadding.calculateTopPadding())
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            when (settingsUiState) {
                SettingsUiState.Loading -> LoadingState(modifier = Modifier.fillMaxSize())
                is SettingsUiState.Success -> {
                    General(
                        languageTag = settingsUiState.languageTag,
                        onLanguageUpdate = onLanguageUpdate,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                    Audio(
                        audioSessionId = settingsUiState.audioSessionId,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                    Appearance(
                        darkThemeConfig = settingsUiState.darkThemeConfig,
                        useDynamicColor = settingsUiState.useDynamicColor,
                        onDarkThemeConfigUpdate = onDarkThemeConfigUpdate,
                        onDynamicColorPreferenceUpdate = onDynamicColorPreferenceUpdate,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                    About(
                        onLicensesClick = onLicensesClick,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                    Spacer(
                        Modifier
                            .navigationBarsPadding()
                            .padding(bottom = 104.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun General(
    languageTag: String,
    onLanguageUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
    ) {
        SectionTitle(
            titleRes = localesR.string.core_locales_general,
            modifier = Modifier.padding(start = 16.dp, bottom = 10.dp, top = 16.dp),
        )
        var shouldShowLanguageDialog by rememberSaveable { mutableStateOf(false) }
        val availableLanguages = listOf(
            Language(
                code = "",
                displayName = stringResource(localesR.string.core_locales_system_default),
                icon = MuzIcons.Rounded.Android,
            ),
            Language(
                code = "en",
                displayName = "English",
                icon = MuzIcons.Filled.UnitedStates,
            ),
            Language(
                code = "ko",
                displayName = "한국어",
                icon = MuzIcons.Filled.SouthKorea,
            ),
            Language(
                code = "ru",
                displayName = "Русский",
                icon = MuzIcons.Filled.Russia,
            ),
        )
        MuzListItem(
            content = { Text(stringResource(localesR.string.core_locales_language)) },
            leadingContent = {
                Icon(
                    imageVector = MuzIcons.Rounded.Language,
                    contentDescription = null,
                )
            },
            shapes = ListItemDefaults.shapes(shape = RoundedCornerShape(16.dp)),
            onClick = { shouldShowLanguageDialog = true },
            supportingContent = {
                Text(
                    text = availableLanguages.find { it.code == languageTag }?.displayName
                        ?: stringResource(localesR.string.core_locales_system_default),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
        )
        if (shouldShowLanguageDialog) {
            LanguageDialog(
                language = languageTag,
                availableLanguages = availableLanguages,
                onLanguageClick = onLanguageUpdate,
                onDismiss = { shouldShowLanguageDialog = false },
            )
        }
    }
}

internal data class Language(
    val code: String,
    val displayName: String,
    val icon: ImageVector,
)

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
            titleRes = localesR.string.core_locales_appearance,
            modifier = Modifier.padding(start = 16.dp, bottom = 10.dp, top = 16.dp),
        )
        var shouldShowThemeDialog by rememberSaveable { mutableStateOf(false) }
        val themeOptions = listOf(
            stringResource(localesR.string.core_locales_system_default) to MuzIcons.Rounded.Android,
            stringResource(localesR.string.core_locales_theme_light) to MuzIcons.Filled.LightMode,
            stringResource(localesR.string.core_locales_theme_dark) to MuzIcons.Filled.DarkMode,
        )
        MuzListItem(
            onClick = { shouldShowThemeDialog = true },
            shapes = if (supportDynamicColor) {
                ListItemDefaults.segmentedShapes(0, 2)
            } else {
                ListItemDefaults.shapes(shape = RoundedCornerShape(16.dp))
            },
            content = { Text(stringResource(localesR.string.core_locales_theme)) },
            leadingContent = {
                Icon(
                    imageVector = MuzIcons.Filled.Palette,
                    contentDescription = null,
                )
            },
            supportingContent = {
                Text(
                    text = themeOptions[darkThemeConfig.ordinal].first,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
        )
        if (shouldShowThemeDialog) {
            ThemeDialog(
                themeConfig = darkThemeConfig,
                themeOptions = themeOptions,
                onThemeConfigUpdate = onDarkThemeConfigUpdate,
                onDismiss = { shouldShowThemeDialog = false },
            )
        }

        if (supportDynamicColor) {
            MuzToggableListItem(
                shapes = ListItemDefaults.segmentedShapes(1, 2),
                content = { Text(stringResource(localesR.string.core_locales_dynamic_color)) },
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
private fun Audio(
    audioSessionId: Int?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
    ) {
        SectionTitle(
            titleRes = localesR.string.core_locales_audio,
            modifier = Modifier.padding(start = 16.dp, bottom = 10.dp, top = 16.dp),
        )
        val context = LocalContext.current
        val equalizerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
        ) { _ -> }

        MuzListItem(
            content = { Text(stringResource(localesR.string.core_locales_equalizer)) },
            leadingContent = {
                Icon(
                    imageVector = MuzIcons.Rounded.GraphicEq,
                    contentDescription = null,
                )
            },
            shapes = ListItemDefaults.shapes(shape = RoundedCornerShape(16.dp)),
            onClick = {
                runCatching {
                    val intent = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
                        .apply {
                            putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSessionId)
                            putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.packageName)
                            putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
                        }
                    equalizerLauncher.launch(intent)
                }.onFailure { error("Failed to open equalizer") }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun About(
    onLicensesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
    ) {
        SectionTitle(
            titleRes = localesR.string.core_locales_about,
            modifier = Modifier.padding(start = 16.dp, bottom = 10.dp, top = 16.dp),
        )
        val context = LocalContext.current
        val backgroundColor = MaterialTheme.colorScheme.background.toArgb()
        MuzListItem(
            content = { Text(stringResource(localesR.string.core_locales_feedback)) },
            leadingContent = {
                Icon(
                    imageVector = MuzIcons.Filled.Feedback,
                    contentDescription = null,
                )
            },
            shapes = ListItemDefaults.segmentedShapes(0, 4),
            onClick = {
                launchBrowserTab(
                    context = context,
                    uri = FEEDBACK_URL.toUri(),
                    toolbarColor = backgroundColor,
                )
            },
        )
        MuzListItem(
            content = { Text(stringResource(localesR.string.core_locales_privacy_policy)) },
            leadingContent = {
                Icon(
                    imageVector = MuzIcons.Filled.Policy,
                    contentDescription = null,
                )
            },
            shapes = ListItemDefaults.segmentedShapes(1, 4),
            onClick = {
                launchBrowserTab(
                    context = context,
                    uri = PRIVACY_POLICY_URL.toUri(),
                    toolbarColor = backgroundColor,
                )
            },
        )
        MuzListItem(
            content = { Text(stringResource(localesR.string.core_locales_licenses)) },
            leadingContent = {
                Icon(
                    imageVector = MuzIcons.Filled.Gavel,
                    contentDescription = null,
                )
            },
            shapes = ListItemDefaults.segmentedShapes(2, 4),
            onClick = onLicensesClick,
        )
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val versionName = packageInfo?.versionName ?: "?.?.?"
        val versionCode = "(${packageInfo?.longVersionCode})"
        MuzListItem(
            content = { Text(stringResource(localesR.string.core_locales_version)) },
            leadingContent = {
                Icon(
                    imageVector = MuzIcons.Filled.Info,
                    contentDescription = null,
                )
            },
            supportingContent = { Text("$versionName $versionCode") },
            shapes = ListItemDefaults.segmentedShapes(3, 4),
        )
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

private fun launchBrowserTab(
    context: Context,
    uri: Uri,
    @ColorInt toolbarColor: Int,
) {
    val customTabBarColor = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(toolbarColor)
        .build()
    val customTabsIntent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(customTabBarColor)
        .build()
    customTabsIntent.launchUrl(context, uri)
}

private const val FEEDBACK_URL =
    "https://trusted-cowl-779.notion.site/31c66ebc684d812c9161eef501af353a?pvs=105"
private const val PRIVACY_POLICY_URL =
    "https://trusted-cowl-779.notion.site/Privacy-Policy-31c66ebc684d80d68036c7d79bf1c6fe"
