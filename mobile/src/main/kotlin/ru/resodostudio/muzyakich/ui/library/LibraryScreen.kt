package ru.resodostudio.muzyakich.ui.library

import androidx.activity.compose.LocalActivity
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.PrimaryTabRow
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.serialization.Serializable
import ru.resodostudio.cashsense.core.ui.LoadingState
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Settings
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Album
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ApkInstall
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Download
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.core.navigation.rememberNavigationState
import ru.resodostudio.muzyakich.core.navigation.toEntries
import ru.resodostudio.muzyakich.ui.album.list.navigation.AlbumsNavKey
import ru.resodostudio.muzyakich.ui.album.list.navigation.albumsEntry
import ru.resodostudio.muzyakich.ui.artist.list.navigation.ArtistsNavKey
import ru.resodostudio.muzyakich.ui.artist.list.navigation.artistsEntry
import ru.resodostudio.muzyakich.ui.song.list.navigation.SongsNavKey
import ru.resodostudio.muzyakich.ui.song.list.navigation.songsEntry
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Serializable
data object PlaylistsNavKey : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onAlbumClick: (Long) -> Unit,
    onArtistClick: (Long) -> Unit,
    onSongMenuClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val inAppUpdateState by viewModel.inAppUpdateState.collectAsStateWithLifecycle()

    LibraryScreen(
        inAppUpdateState = inAppUpdateState,
        onAlbumClick = onAlbumClick,
        onArtistClick = onArtistClick,
        onSongMenuClick = onSongMenuClick,
        onSettingsClick = onSettingsClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LibraryScreen(
    inAppUpdateState: AppUpdateResult,
    onAlbumClick: (Long) -> Unit,
    onArtistClick: (Long) -> Unit,
    onSongMenuClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LibraryTopAppBar(
                inAppUpdateState = inAppUpdateState,
                titleRes = localesR.string.app_name,
                onSettingsClick = onSettingsClick,
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
        ) {
            val libraryTabs = LibraryTab.entries
            val navigationState = rememberNavigationState(
                initialBackStack = listOf(libraryTabs.first().navKey),
            )
            val navigator = remember { Navigator(navigationState) }
            val currentTab = libraryTabs.find { it.navKey == navigationState.backStack.last() }
                ?: libraryTabs.first()

            PrimaryTabRow(
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
                entry<PlaylistsNavKey> {
                    LoadingState(Modifier.fillMaxSize())
                }
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
                contentDescription = stringResource(localesR.string.settings),
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
                    is AppUpdateResult.Available -> localesR.string.download_update
                    is AppUpdateResult.InProgress -> localesR.string.downloading_update
                    else -> localesR.string.install_update
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
                                IconButtonDefaults.IconButtonWidthOption.Wide
                            )
                        ),
                    ) {
                        if (inAppUpdateState is AppUpdateResult.InProgress) {
                            val downloaded = inAppUpdateState.installState.bytesDownloaded().toFloat()
                            val total = inAppUpdateState.installState.totalBytesToDownload().toFloat()

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
        }
    )
}

private enum class LibraryTab(
    @StringRes val titleRes: Int,
    val icon: ImageVector,
    val navKey: NavKey,
) {
    //PLAYLISTS(localesR.string.playlists, MuzIcons.Rounded.LibraryMusic, PlaylistsNavKey),
    SONGS(localesR.string.songs, MuzIcons.Rounded.MusicNote, SongsNavKey),
    ALBUMS(localesR.string.albums, MuzIcons.Rounded.Album, AlbumsNavKey),
    ARTISTS(localesR.string.artists, MuzIcons.Rounded.Artist, ArtistsNavKey),
}