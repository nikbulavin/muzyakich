package ru.resodostudio.muzyakich.ui.library

import androidx.activity.compose.LocalActivity
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.google.android.play.core.ktx.AppUpdateResult
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Settings
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Add
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Album
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ApkInstall
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Download
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.LibraryMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.core.navigation.rememberNavigationState
import ru.resodostudio.muzyakich.core.navigation.toEntries
import ru.resodostudio.muzyakich.feature.playlist.list.api.PlaylistsNavKey
import ru.resodostudio.muzyakich.feature.playlist.list.impl.navigation.playlistsEntry
import ru.resodostudio.muzyakich.feature.song.list.api.SongsNavKey
import ru.resodostudio.muzyakich.feature.song.list.impl.navigation.songsEntry
import ru.resodostudio.muzyakich.ui.album.list.navigation.AlbumsNavKey
import ru.resodostudio.muzyakich.ui.album.list.navigation.albumsEntry
import ru.resodostudio.muzyakich.ui.artist.list.navigation.ArtistsNavKey
import ru.resodostudio.muzyakich.ui.artist.list.navigation.artistsEntry
import kotlin.uuid.Uuid
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun LibraryScreen(
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
        onPlaylistClick = onPlaylistClick,
        onAlbumClick = onAlbumClick,
        onArtistClick = onArtistClick,
        onSongMenuClick = onSongMenuClick,
        onSettingsClick = onSettingsClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LibraryScreen(
    libraryUiState: LibraryUiState,
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
            val navigationState = rememberNavigationState(
                initialBackStack = listOf(libraryTabs.first().navKey),
            )
            val navigator = remember { Navigator(navigationState) }
            val currentTab = libraryTabs.find { it.navKey == navigationState.backStack.last() }
                ?: libraryTabs.first()

            val bottomPadding by animateDpAsState(
                targetValue = (if (libraryUiState.isCurrentMediaItemExists) 88.dp else 0.dp) +
                        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
            )

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
                floatingActionButton = {
                    val contentDescription =
                        stringResource(localesR.string.core_locales_new_playlist)
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                            TooltipAnchorPosition.Above,
                        ),
                        tooltip = { PlainTooltip { Text(contentDescription) } },
                        state = rememberTooltipState(),
                        modifier = Modifier
                            .windowInsetsPadding(
                                WindowInsets.safeDrawing.only(
                                    WindowInsetsSides.Horizontal,
                                ),
                            )
                            .padding(bottom = bottomPadding)
                            .animateFloatingActionButton(
                                visible = PlaylistsNavKey in navigationState.backStack,
                                alignment = Alignment.BottomEnd,
                            ),
                    ) {
                        FloatingActionButton(onClick = { /* do something */ }) {
                            Icon(
                                imageVector = MuzIcons.Rounded.Add,
                                contentDescription = contentDescription,
                            )
                        }
                    }
                },
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
                                onClick = { navigator.navigateAndClearStack(tab.navKey) },
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
                    val entryProvider = entryProvider {
                        playlistsEntry(onPlaylistClick)
                        songsEntry(onSongMenuClick)
                        albumsEntry(onAlbumClick)
                        artistsEntry(onArtistClick)
                    }

                    val motionScheme = MaterialTheme.motionScheme

                    NavDisplay(
                        entries = navigationState.toEntries(entryProvider),
                        onBack = navigator::goBack,
                        transitionSpec = {
                            scaleIn(motionScheme.defaultSpatialSpec(), 0.92f) +
                                    fadeIn(motionScheme.defaultEffectsSpec()) togetherWith
                                    fadeOut(snap())
                        },
                        popTransitionSpec = {
                            scaleIn(motionScheme.defaultSpatialSpec(), 0.92f) +
                                    fadeIn(motionScheme.defaultEffectsSpec()) togetherWith
                                    fadeOut(snap())
                        },
                        predictivePopTransitionSpec = {
                            scaleIn(motionScheme.defaultSpatialSpec(), 0.92f) +
                                    fadeIn(motionScheme.defaultEffectsSpec()) togetherWith
                                    fadeOut(snap())
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
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

enum class LibraryTab(
    @StringRes val titleRes: Int,
    val icon: ImageVector,
    val navKey: NavKey,
) {
    PLAYLISTS(
        titleRes = localesR.string.core_locales_playlists,
        icon = MuzIcons.Rounded.LibraryMusic,
        navKey = PlaylistsNavKey,
    ),
    SONGS(
        titleRes = localesR.string.core_locales_songs,
        icon = MuzIcons.Rounded.MusicNote,
        navKey = SongsNavKey,
    ),
    ALBUMS(
        titleRes = localesR.string.core_locales_albums,
        icon = MuzIcons.Rounded.Album,
        navKey = AlbumsNavKey,
    ),
    ARTISTS(
        titleRes = localesR.string.core_locales_artists,
        icon = MuzIcons.Rounded.Artist,
        navKey = ArtistsNavKey,
    ),
}