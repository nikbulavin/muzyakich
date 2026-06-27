package ru.resodostudio.muzyakich.ui.library

import androidx.activity.compose.LocalActivity
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.play.core.ktx.AppUpdateResult
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Settings
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ApkInstall
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Download
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.playlist.list.api.PlaylistsNavKey
import ru.resodostudio.muzyakich.feature.playlist.list.impl.navigation.PlaylistsEntry
import ru.resodostudio.muzyakich.feature.song.list.api.SongsNavKey
import ru.resodostudio.muzyakich.feature.song.list.impl.navigation.SongsEntry
import ru.resodostudio.muzyakich.ui.album.list.navigation.AlbumsEntry
import ru.resodostudio.muzyakich.ui.album.list.navigation.AlbumsNavKey
import ru.resodostudio.muzyakich.ui.artist.list.navigation.ArtistsEntry
import ru.resodostudio.muzyakich.ui.artist.list.navigation.ArtistsNavKey
import kotlin.uuid.Uuid
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun LibraryScreen(
    libraryNavigator: Navigator,
    onPlaylistClick: (Uuid) -> Unit,
    onAlbumClick: (Long) -> Unit,
    onArtistClick: (Long) -> Unit,
    onSongMenuClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val libraryUiState by viewModel.libraryUiState.collectAsStateWithLifecycle()

    LibraryScreen(
        libraryUiState = libraryUiState,
        libraryNavigator = libraryNavigator,
        onPlaylistClick = onPlaylistClick,
        onAlbumClick = onAlbumClick,
        onArtistClick = onArtistClick,
        onSongMenuClick = onSongMenuClick,
        onSettingsClick = onSettingsClick,
        modifier = modifier,
    )
}

@Composable
private fun LibraryScreen(
    libraryUiState: LibraryUiState,
    libraryNavigator: Navigator,
    onPlaylistClick: (Uuid) -> Unit,
    onAlbumClick: (Long) -> Unit,
    onArtistClick: (Long) -> Unit,
    onSongMenuClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (libraryUiState) {
        LibraryUiState.Error, LibraryUiState.Loading -> Unit
        is LibraryUiState.Success -> {
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

            val libraryTabs = LibraryTab.entries
            val currentTab = libraryTabs.find { it.navKey == libraryNavigator.state.backStack.last() }
                ?: libraryTabs.first()

            Scaffold(
                modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    LibraryTopAppBar(
                        inAppUpdateState = libraryUiState.appUpdateResult,
                        titleRes = localesR.string.core_locales_app_name,
                        onSettingsClick = onSettingsClick,
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.topAppBarColors().copy(
                            scrolledContainerColor = MaterialTheme.colorScheme.surface,
                        ),
                    )
                },
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
            ) { paddingValues ->
                Column(
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                ) {
                    PrimaryScrollableTabRow(
                        selectedTabIndex = currentTab.ordinal,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        libraryTabs.forEach { tab ->
                            Tab(
                                selected = currentTab == tab,
                                onClick = { libraryNavigator.navigateAndClearStack(tab.navKey) },
                                icon = {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = null,
                                    )
                                },
                                text = {
                                    Text(
                                        text = stringResource(tab.titleRes),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                },
                            )
                        }
                    }
                    val motionScheme = MaterialTheme.motionScheme
                    AnimatedContent(
                        targetState = libraryNavigator.state.backStack.last(),
                        transitionSpec = {
                            scaleIn(motionScheme.defaultSpatialSpec(), 0.92f) +
                                    fadeIn(motionScheme.defaultEffectsSpec()) togetherWith
                                    fadeOut(snap())
                        },
                    ) { state ->
                        when (state) {
                            PlaylistsNavKey -> PlaylistsEntry(onPlaylistClick)
                            SongsNavKey -> SongsEntry(onSongMenuClick)
                            AlbumsNavKey -> AlbumsEntry(onAlbumClick)
                            ArtistsNavKey -> ArtistsEntry(onArtistClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryTopAppBar(
    @StringRes titleRes: Int,
    inAppUpdateState: AppUpdateResult,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(titleRes)) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = colors,
        navigationIcon = {
            MuzIconButton(
                onClick = onSettingsClick,
                icon = MuzIcons.Filled.Settings,
                contentDescription = stringResource(localesR.string.core_locales_settings),
                tooltipPosition = TooltipAnchorPosition.Right,
            )
        },
        actions = {
            AnimatedVisibility(
                visible = inAppUpdateState !is AppUpdateResult.NotAvailable,
                modifier = Modifier.padding(end = 8.dp),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                val contentDescriptionRes = when (inAppUpdateState) {
                    is AppUpdateResult.Available -> localesR.string.core_locales_download_update
                    is AppUpdateResult.InProgress -> localesR.string.core_locales_downloading_update
                    else -> localesR.string.core_locales_install_update
                }
                val activity = LocalActivity.current
                val scope = rememberCoroutineScope()
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                        positioning = TooltipAnchorPosition.Left,
                    ),
                    tooltip = { PlainTooltip { Text(stringResource(contentDescriptionRes)) } },
                    state = rememberTooltipState(),
                ) {
                    FilledTonalIconButton(
                        onClick = {
                            runCatching {
                                if (inAppUpdateState is AppUpdateResult.Available) {
                                    activity?.let { inAppUpdateState.startFlexibleUpdate(it, 1) }
                                }
                                if (inAppUpdateState is AppUpdateResult.Downloaded) {
                                    scope.launch { inAppUpdateState.completeUpdate() }
                                }
                            }.onFailure { error("Application in-app update was failed.") }
                        },
                        shapes = IconButtonDefaults.shapes(),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(),
                        enabled = inAppUpdateState !is AppUpdateResult.InProgress,
                        modifier = Modifier.size(
                            IconButtonDefaults.smallContainerSize(
                                IconButtonDefaults.IconButtonWidthOption.Wide,
                            ),
                        ),
                    ) {
                        if (inAppUpdateState is AppUpdateResult.InProgress) {
                            val downloaded =
                                inAppUpdateState.installState.bytesDownloaded().toFloat()
                            val total =
                                inAppUpdateState.installState.totalBytesToDownload().toFloat()

                            val animatedProgress by animateFloatAsState(
                                targetValue = if (total > 0f) downloaded / total else 0f,
                                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                                label = "InAppUpdateProgress",
                            )

                            CircularProgressIndicator(
                                progress = { animatedProgress },
                                modifier = Modifier.size(24.dp),
                            )
                        } else {
                            Icon(
                                imageVector = if (inAppUpdateState is AppUpdateResult.Available) {
                                    MuzIcons.Rounded.Download
                                } else {
                                    MuzIcons.Rounded.ApkInstall
                                },
                                contentDescription = stringResource(contentDescriptionRes),
                            )
                        }
                    }
                }
            }
        },
    )
}
