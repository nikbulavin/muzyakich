package ru.resodostudio.muzyakich.ui.library

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSelectableListItem
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
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.core.navigation.rememberNavigationState
import ru.resodostudio.muzyakich.core.navigation.toEntries
import ru.resodostudio.muzyakich.ui.component.EmptyState
import ru.resodostudio.muzyakich.ui.component.LoadingState
import ru.resodostudio.muzyakich.ui.component.songs
import ru.resodostudio.muzyakich.ui.component.songsInfo
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Serializable
data object PlaylistsNavKey : NavKey

@Serializable
data object SongsNavKey : NavKey

@Serializable
data object AlbumsNavKey : NavKey

@Serializable
data object ArtistsNavKey : NavKey

@Composable
fun LibraryScreen(
    onArtistClick: (Long) -> Unit,
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val libraryUiState by viewModel.libraryUiState.collectAsStateWithLifecycle()

    LibraryScreen(
        libraryUiState = libraryUiState,
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
)
@Composable
private fun LibraryScreen(
    libraryUiState: LibraryUiState,
    onArtistClick: (Long) -> Unit,
    onPlaySongsClick: (songs: List<Song>, startIndex: Int) -> Unit = { _, _ -> },
    onShuffleSongsClick: (songs: List<Song>, startIndex: Int) -> Unit = { _, _ -> },
    onToggleFilterFavorites: (Boolean) -> Unit = {},
    onPlayNextClick: (Song) -> Unit = {},
    onSortByUpdate: (SortBy) -> Unit = {},
    onSortOrderUpdate: (SortOrder) -> Unit = {},
) {
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
            val libraryTabs = LibraryTab.entries

            val navigationState = rememberNavigationState(
                initialBackStack = listOf(libraryTabs.first().navKey),
            )
            val navigator = remember { Navigator(navigationState) }
            val currentTab = libraryTabs.find { it.navKey == navigationState.backStack.last() }
                ?: libraryTabs.first()

            PrimaryScrollableTabRow(
                selectedTabIndex = currentTab.ordinal,
                modifier = Modifier.fillMaxWidth(),
            ) {
                libraryTabs.forEach { tab ->
                    Tab(
                        selected = currentTab == tab,
                        onClick = { navigator.navigate(tab.navKey) },
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

                    val entryProvider = entryProvider {
                        entry<PlaylistsNavKey> {
                            LoadingState(Modifier.fillMaxSize())
                        }
                        entry<SongsNavKey> {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(300.dp),
                                contentPadding = PaddingValues(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 8.dp,
                                    bottom = 104.dp + paddingValues.calculateBottomPadding(),
                                ),
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
                            ) {
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
                        }
                        entry<AlbumsNavKey> {
                            LoadingState(Modifier.fillMaxSize())
                        }
                        entry<ArtistsNavKey> {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(300.dp),
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
                                contentPadding = PaddingValues(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 16.dp,
                                    bottom = 104.dp + paddingValues.calculateBottomPadding(),
                                ),
                            ) {
                                artists(
                                    artists = libraryUiState.artists,
                                    onArtistClick = onArtistClick,
                                )
                            }
                        }
                    }

                    NavDisplay(
                        entries = navigationState.toEntries(entryProvider),
                        onBack = navigator::goBack,
                    )
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
            MuzIconButton(
                onClick = onFilterClick,
                icon = MuzIcons.Rounded.FilterList,
                contentDescription = stringResource(localesR.string.open_filter_menu),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun LazyGridScope.artists(
    artists: List<Artist>,
    onArtistClick: (Long) -> Unit,
) {
    itemsIndexed(
        items = artists,
        key = { _, artist -> artist.id },
        contentType = { _, _ -> "Artist" },
    ) { index, artist ->
        MuzSelectableListItem(
            shapes = ListItemDefaults.segmentedShapes(index, artists.size),
            selected = false,
            content = {
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
            onClick = { onArtistClick(artist.id) },
            modifier = Modifier.animateItem(),
        )
    }
}

enum class LibraryTab(
    @StringRes val titleRes: Int,
    val icon: ImageVector,
    val navKey: NavKey,
) {
    PLAYLISTS(localesR.string.playlists, MuzIcons.Rounded.LibraryMusic, PlaylistsNavKey),
    SONGS(localesR.string.songs, MuzIcons.Rounded.MusicNote, SongsNavKey),
    ALBUMS(localesR.string.albums, MuzIcons.Rounded.Album, AlbumsNavKey),
    ARTISTS(localesR.string.artists, MuzIcons.Rounded.Artist, ArtistsNavKey),
}