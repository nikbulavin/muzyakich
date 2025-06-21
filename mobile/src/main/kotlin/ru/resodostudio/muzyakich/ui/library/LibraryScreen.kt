package ru.resodostudio.muzyakich.ui.library

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.FloatingToolbarDefaults.floatingToolbarVerticalNestedScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.resodostudio.muzyakich.core.designsystem.component.MuzTopAppBar
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Album
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.LibraryMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.ui.component.EmptyState
import ru.resodostudio.muzyakich.ui.component.LoadingState
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
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LibraryScreen(
    libraryUiState: LibraryUiState,
    onNowPlayingBarClick: () -> Unit,
    onPlaySongsClick: (songs: List<Song>, startIndex: Int) -> Unit = { _, _ -> },
    onShuffleSongsClick: (songs: List<Song>, startIndex: Int) -> Unit = { _, _ -> },
    onPlayClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onSkipNextClick: () -> Unit = {},
) {
    val tabs = listOf(
        TabItem(stringResource(localesR.string.playlists), MuzIcons.Rounded.LibraryMusic),
        TabItem(stringResource(localesR.string.songs), MuzIcons.Rounded.MusicNote),
        TabItem(stringResource(localesR.string.albums), MuzIcons.Rounded.Album),
        TabItem(stringResource(localesR.string.artists), MuzIcons.Rounded.Artist),
    )
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    Column {
        MuzTopAppBar(
            titleRes = localesR.string.app_name,
        )
        PrimaryScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = null,
                        )
                    },
                    text = {
                        Text(
                            text = tab.title,
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
                    AnimatedContent(selectedTabIndex) { state ->
                        when (state) {
                            0 -> Unit
                            1 -> {
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
                                    items(songs) { song ->
                                        val isPlaying =
                                            libraryUiState.nowPlayingState.mediaId == song.mediaId &&
                                                    libraryUiState.nowPlayingState.playWhenReady

                                        SongItem(
                                            song = song,
                                            isPlaying = isPlaying,
                                            modifier = Modifier.animateItem(),
                                            onClick = { onPlaySongsClick(songs, songs.indexOf(song)) },
                                        )
                                    }
                                }
                            }

                            2 -> Unit
                            3 -> Unit
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

data class TabItem(
    val title: String,
    val icon: ImageVector,
)