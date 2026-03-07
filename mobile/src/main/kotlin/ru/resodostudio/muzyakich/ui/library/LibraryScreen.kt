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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
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
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Album
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ApkInstall
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Download
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.FilterList
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlayArrow
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Shuffle
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.core.navigation.rememberNavigationState
import ru.resodostudio.muzyakich.core.navigation.toEntries
import ru.resodostudio.muzyakich.ui.album.list.navigation.AlbumsNavKey
import ru.resodostudio.muzyakich.ui.album.list.navigation.albumsEntry
import ru.resodostudio.muzyakich.ui.artist.list.navigation.ArtistsNavKey
import ru.resodostudio.muzyakich.ui.artist.list.navigation.artistsEntry
import ru.resodostudio.muzyakich.ui.component.LoadingState
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
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val inAppUpdateState by viewModel.inAppUpdateState.collectAsStateWithLifecycle()

    LibraryScreen(
        inAppUpdateState = inAppUpdateState,
        onAlbumClick = onAlbumClick,
        onArtistClick = onArtistClick,
        onSongMenuClick = onSongMenuClick,
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
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LibraryTopAppBar(
                inAppUpdateState = inAppUpdateState,
                titleRes = localesR.string.app_name,
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun LazyGridScope.actionButtons(
    songs: List<Song>,
    onPlaySongsClick: (List<Song>, Int) -> Unit,
    onShuffleSongsClick: (List<Song>, Int) -> Unit,
    onFilterClick: () -> Unit,
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { "ActionButtons" },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp - ListItemDefaults.SegmentedGap)
                .animateItem(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                shapes = ButtonDefaults.shapes(),
                onClick = { onPlaySongsClick(songs, DEFAULT_INDEX) },
                modifier = Modifier.weight(1f),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ButtonDefaults.IconSpacing),
                ) {
                    Icon(
                        imageVector = MuzIcons.Rounded.PlayArrow,
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(localesR.string.play_audio),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            OutlinedButton(
                shapes = ButtonDefaults.shapes(),
                onClick = { onShuffleSongsClick(songs, DEFAULT_INDEX) },
                modifier = Modifier.weight(1f),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ButtonDefaults.IconSpacing),
                ) {
                    Icon(
                        imageVector = MuzIcons.Rounded.Shuffle,
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource(localesR.string.shuffle),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            MuzIconButton(
                onClick = onFilterClick,
                icon = MuzIcons.Rounded.FilterList,
                contentDescription = stringResource(localesR.string.open_filter_menu),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LibraryTopAppBar(
    @StringRes titleRes: Int,
    inAppUpdateState: AppUpdateResult,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(titleRes)) },
        modifier = modifier.testTag("libraryTopAppBar"),
        scrollBehavior = scrollBehavior,
        colors = colors,
        actions = {
            AnimatedVisibility(
                visible = inAppUpdateState !is AppUpdateResult.NotAvailable,
                modifier = Modifier.padding(end = 16.dp),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                val activity = LocalActivity.current
                val scope = rememberCoroutineScope()
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                        positioning = TooltipAnchorPosition.Left,
                    ),
                    tooltip = { PlainTooltip { Text("Download update") } },
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
                                contentDescription = "Download update",
                            )
                        }
                    }
                }
            }
        }
    )
}

enum class LibraryTab(
    @StringRes val titleRes: Int,
    val icon: ImageVector,
    val navKey: NavKey,
) {
    //PLAYLISTS(localesR.string.playlists, MuzIcons.Rounded.LibraryMusic, PlaylistsNavKey),
    SONGS(localesR.string.songs, MuzIcons.Rounded.MusicNote, SongsNavKey),
    ALBUMS(localesR.string.albums, MuzIcons.Rounded.Album, AlbumsNavKey),
    ARTISTS(localesR.string.artists, MuzIcons.Rounded.Artist, ArtistsNavKey),
}