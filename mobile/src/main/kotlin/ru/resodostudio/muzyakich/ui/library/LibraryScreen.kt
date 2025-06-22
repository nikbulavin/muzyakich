package ru.resodostudio.muzyakich.ui.library

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.FloatingToolbarDefaults.floatingToolbarVerticalNestedScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.resodostudio.muzyakich.core.designsystem.component.MuzTopAppBar
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Album
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Check
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.LibraryMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Star
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.ui.component.EmptyState
import ru.resodostudio.muzyakich.ui.component.LoadingState
import ru.resodostudio.muzyakich.ui.library.LibraryTab.ALBUMS
import ru.resodostudio.muzyakich.ui.library.LibraryTab.ARTISTS
import ru.resodostudio.muzyakich.ui.library.LibraryTab.PLAYLISTS
import ru.resodostudio.muzyakich.ui.library.LibraryTab.SONGS
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun LibraryScreen(
    onNowPlayingBarClick: () -> Unit,
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val libraryUiState by viewModel.libraryUiState.collectAsStateWithLifecycle()

    LibraryScreen(
        libraryUiState = libraryUiState,
        onNowPlayingBarClick = onNowPlayingBarClick,
        onPlaySongsClick = viewModel::playSongs,
        onShuffleSongsClick = viewModel::shuffleSongs,
        onPlayClick = viewModel::play,
        onPauseClick = viewModel::pause,
        onSkipNextClick = viewModel::skipNext,
        onToggleFilterFavorites = viewModel::toggleFilterFavorites,
    )
}

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
private fun LibraryScreen(
    libraryUiState: LibraryUiState,
    onNowPlayingBarClick: () -> Unit,
    onPlaySongsClick: (songs: List<Song>, startIndex: Int) -> Unit = { _, _ -> },
    onShuffleSongsClick: (songs: List<Song>, startIndex: Int) -> Unit = { _, _ -> },
    onPlayClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onSkipNextClick: () -> Unit = {},
    onToggleFilterFavorites: () -> Unit = {},
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
            val libraryTabs = remember { LibraryTab.entries }
            var selectedTab by rememberSaveable { mutableStateOf(libraryTabs.first()) }

            PrimaryScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                modifier = Modifier.fillMaxWidth(),
            ) {
                libraryTabs.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
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
                    val songs = libraryUiState.songs
                    var expanded by rememberSaveable { mutableStateOf(true) }

                    Box {
                        AnimatedContent(selectedTab) { currentTab ->
                            when (currentTab) {
                                PLAYLISTS -> {
                                    LoadingState(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .navigationBarsPadding(),
                                    )
                                }

                                SONGS -> {
                                    LazyVerticalGrid(
                                        columns = GridCells.Adaptive(300.dp),
                                        contentPadding = PaddingValues(
                                            top = 8.dp,
                                            bottom = 104.dp + WindowInsets.navigationBars
                                                .asPaddingValues()
                                                .calculateBottomPadding(),
                                        ),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .floatingToolbarVerticalNestedScroll(
                                                expanded = expanded,
                                                onExpand = { expanded = true },
                                                onCollapse = { expanded = false },
                                            ),
                                    ) {
                                        item(
                                            span = { GridItemSpan(maxLineSpan) },
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            ) {
                                                FilterChip(
                                                    selected = libraryUiState.shouldFilterFavorites,
                                                    onClick = onToggleFilterFavorites,
                                                    label = { Text(stringResource(localesR.string.favorites)) },
                                                    modifier = Modifier.padding(start = 16.dp),
                                                    leadingIcon = {
                                                        val icon = if (libraryUiState.shouldFilterFavorites) {
                                                            MuzIcons.Rounded.Check
                                                        } else {
                                                            MuzIcons.Rounded.Star
                                                        }
                                                        Icon(
                                                            imageVector = icon,
                                                            contentDescription = null,
                                                        )
                                                    },
                                                )
                                            }
                                        }
                                        items(
                                            items = songs,
                                            key = { it.mediaId },
                                            contentType = { "Song" },
                                        ) { song ->
                                            val isPlaying =
                                                libraryUiState.nowPlayingState.mediaId == song.mediaId &&
                                                        libraryUiState.nowPlayingState.playWhenReady

                                            SongItem(
                                                song = song,
                                                isPlaying = isPlaying,
                                                modifier = Modifier.animateItem(),
                                                onClick = {
                                                    onPlaySongsClick(
                                                        songs,
                                                        songs.indexOf(song)
                                                    )
                                                },
                                            )
                                        }
                                    }
                                }

                                ALBUMS -> {
                                    LoadingState(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .navigationBarsPadding(),
                                    )
                                }

                                ARTISTS -> {
                                    LoadingState(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .navigationBarsPadding(),
                                    )
                                }
                            }
                        }

                        val fastAnimationSpec = MaterialTheme.motionScheme.fastSpatialSpec<Float>()
                        AnimatedContent(
                            targetState = libraryUiState.nowPlayingState.mediaId.isNotBlank(),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .navigationBarsPadding(),
                            contentAlignment = Alignment.Center,
                            transitionSpec = {
                                fadeIn() + scaleIn(fastAnimationSpec, 0.92f) togetherWith fadeOut(snap())
                            },
                        ) { isPlaying ->
                            if (libraryUiState.currentSong != null && isPlaying) {
                                NowPlayingBar(
                                    nowPlayingState = libraryUiState.nowPlayingState,
                                    song = libraryUiState.currentSong,
                                    currentPosition = libraryUiState.currentPosition,
                                    modifier = Modifier.padding(16.dp),
                                    onPlayClick = onPlayClick,
                                    onPauseClick = onPauseClick,
                                    onSkipNextClick = onSkipNextClick,
                                    onClick = onNowPlayingBarClick,
                                )
                            } else {
                                LibraryToolbar(
                                    expanded = expanded,
                                    onPlaySongsClick = onPlaySongsClick,
                                    onShuffleSongsClick = onShuffleSongsClick,
                                    songs = songs,
                                    modifier = Modifier.offset(y = -ScreenOffset),
                                )
                            }
                        }
                    }
                }
            }
        }
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