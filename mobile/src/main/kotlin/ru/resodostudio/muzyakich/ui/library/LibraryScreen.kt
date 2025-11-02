package ru.resodostudio.muzyakich.ui.library

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.designsystem.component.MuzTopAppBar
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Album
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.FilterList
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.LibraryMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlayArrow
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Shuffle
import ru.resodostudio.muzyakich.core.model.data.Artist
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import ru.resodostudio.muzyakich.ui.component.EmptyState
import ru.resodostudio.muzyakich.ui.component.LoadingState
import ru.resodostudio.muzyakich.ui.component.songs
import ru.resodostudio.muzyakich.ui.component.songsInfo
import ru.resodostudio.muzyakich.ui.library.LibraryTab.ALBUMS
import ru.resodostudio.muzyakich.ui.library.LibraryTab.ARTISTS
import ru.resodostudio.muzyakich.ui.library.LibraryTab.PLAYLISTS
import ru.resodostudio.muzyakich.ui.library.LibraryTab.SONGS
import ru.resodostudio.muzyakich.ui.player.NowPlayingBar
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun LibraryScreen(
    onNowPlayingBarClick: () -> Unit,
    onArtistClick: (Long) -> Unit,
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val libraryUiState by viewModel.libraryUiState.collectAsStateWithLifecycle()

    LibraryScreen(
        libraryUiState = libraryUiState,
        onNowPlayingBarClick = onNowPlayingBarClick,
        onArtistClick = onArtistClick,
        onPlaySongsClick = viewModel::playSongs,
        onShuffleSongsClick = viewModel::shuffleSongs,
        onToggleFilterFavorites = viewModel::toggleFilterFavorites,
        onPlayNextClick = viewModel::playSongNext,
        onSortByUpdate = viewModel::updateSortByPreference,
        onSortOrderUpdate = viewModel::updateSortOrderPreference,
    )
}

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalHazeMaterialsApi::class,
    ExperimentalHazeApi::class,
)
@Composable
private fun LibraryScreen(
    libraryUiState: LibraryUiState,
    onNowPlayingBarClick: () -> Unit,
    onArtistClick: (Long) -> Unit,
    onPlaySongsClick: (songs: List<Song>, startIndex: Int) -> Unit = { _, _ -> },
    onShuffleSongsClick: (songs: List<Song>, startIndex: Int) -> Unit = { _, _ -> },
    onToggleFilterFavorites: (Boolean) -> Unit = {},
    onPlayNextClick: (Song) -> Unit = {},
    onSortByUpdate: (SortBy) -> Unit = {},
    onSortOrderUpdate: (SortOrder) -> Unit = {},
) {
    val hazeState = rememberHazeState()
    val nowPlayingBarHazeStyle = HazeMaterials.ultraThin(MaterialTheme.colorScheme.surfaceContainer)
    val hazeStyle = HazeMaterials.ultraThin()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MuzTopAppBar(
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
            val lazyGridState = rememberLazyGridState()
            val coroutineScope = rememberCoroutineScope()

            val libraryTabs = remember { LibraryTab.entries }
            var selectedTab by rememberSaveable { mutableStateOf(libraryTabs.first()) }

            PrimaryScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                containerColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .hazeEffect(hazeState, hazeStyle) {
                        inputScale = HazeInputScale.Auto
                        blurEnabled = true
                        noiseFactor = 0f
                    },
            ) {
                libraryTabs.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = {
                            selectedTab = tab
                            coroutineScope.launch {
                                lazyGridState.scrollToItem(0)
                            }
                        },
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
            when (libraryUiState) {
                LibraryUiState.Loading -> LoadingState(Modifier.fillMaxSize())
                LibraryUiState.Empty -> {
                    EmptyState(
                        messageRes = localesR.string.library_empty,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                            .navigationBarsPadding(),
                    )
                }

                is LibraryUiState.Success -> {
                    var shouldShowFilterBottomSheet by rememberSaveable { mutableStateOf(false) }

                    if (shouldShowFilterBottomSheet) {
                        FilterBottomSheet(
                            filterConfig = libraryUiState.filterConfig,
                            onSortByUpdate = onSortByUpdate,
                            onSortOrderUpdate = onSortOrderUpdate,
                            onDismiss = { shouldShowFilterBottomSheet = false },
                            onToggleFilterFavorites = onToggleFilterFavorites,
                        )
                    }

                    Box {
                        LazyVerticalGrid(
                            state = lazyGridState,
                            columns = GridCells.Adaptive(300.dp),
                            contentPadding = PaddingValues(
                                top = 8.dp,
                                bottom = 104.dp + paddingValues.calculateBottomPadding(),
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                                .hazeSource(hazeState),
                        ) {
                            when (selectedTab) {
                                PLAYLISTS -> {
                                    item(
                                        span = { GridItemSpan(maxLineSpan) },
                                    ) {
                                        LoadingState(modifier = Modifier.animateItem())
                                    }
                                }

                                SONGS -> {
                                    actionButtons(
                                        songs = libraryUiState.songs,
                                        onPlaySongsClick = onPlaySongsClick,
                                        onShuffleSongsClick = onShuffleSongsClick,
                                        onFilterClick = { shouldShowFilterBottomSheet = true },
                                    )
                                    songs(
                                        songs = libraryUiState.songs,
                                        nowPlayingState = libraryUiState.nowPlayingState,
                                        onPlaySongsClick = onPlaySongsClick,
                                        onPlayNextClick = onPlayNextClick,
                                    )
                                    songsInfo(
                                        songs = libraryUiState.songs,
                                    )
                                }

                                ALBUMS -> {
                                    item(
                                        span = { GridItemSpan(maxLineSpan) },
                                    ) {
                                        LoadingState(modifier = Modifier.animateItem())
                                    }
                                }

                                ARTISTS -> {
                                    artists(
                                        artists = libraryUiState.artists,
                                        onArtistClick = onArtistClick,
                                    )
                                }
                            }
                        }

                        NowPlayingBar(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.BottomCenter)
                                .navigationBarsPadding()
                                .clip(MaterialTheme.shapes.medium)
                                .hazeEffect(hazeState, nowPlayingBarHazeStyle) {
                                    inputScale = HazeInputScale.Auto
                                    blurEnabled = true
                                    noiseFactor = 0f
                                },
                            onClick = onNowPlayingBarClick,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun LazyGridScope.actionButtons(
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
                .padding(horizontal = 16.dp)
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
                    horizontalArrangement = Arrangement.spacedBy(
                        ButtonDefaults.IconSpacing
                    ),
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
                    horizontalArrangement = Arrangement.spacedBy(
                        ButtonDefaults.IconSpacing
                    ),
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
            IconButton(
                onClick = onFilterClick,
            ) {
                Icon(
                    imageVector = MuzIcons.Rounded.FilterList,
                    contentDescription = stringResource(localesR.string.open_filter_menu),
                )
            }
        }
    }
}

private fun LazyGridScope.artists(
    artists: List<Artist>,
    onArtistClick: (Long) -> Unit,
) {
    items(
        items = artists,
        key = { it.id },
        contentType = { "Artists" },
    ) { artist ->
        ListItem(
            headlineContent = {
                Text(
                    text = artist.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            supportingContent = {
                Text(
                    text = pluralStringResource(
                        localesR.plurals.number_of_songs,
                        artist.songs.size,
                        artist.songs.size,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            modifier = Modifier
                .clickable { onArtistClick(artist.id) }
                .animateItem(),
        )
    }
}

enum class LibraryTab(
    @StringRes val titleRes: Int,
    val icon: ImageVector,
) {
    PLAYLISTS(localesR.string.playlists, MuzIcons.Rounded.LibraryMusic),
    SONGS(localesR.string.songs, MuzIcons.Rounded.MusicNote),
    ALBUMS(localesR.string.albums, MuzIcons.Rounded.Album),
    ARTISTS(localesR.string.artists, MuzIcons.Rounded.Artist),
}