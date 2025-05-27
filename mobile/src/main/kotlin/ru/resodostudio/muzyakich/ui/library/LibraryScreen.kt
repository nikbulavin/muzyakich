package ru.resodostudio.muzyakich.ui.library

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
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
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val libraryUiState by viewModel.libraryUiState.collectAsStateWithLifecycle()

    LibraryScreen(
        libraryUiState = libraryUiState,
        onPlayClick = viewModel::playSongs,
    )
}

@Composable
private fun LibraryScreen(
    libraryUiState: LibraryUiState,
    onPlayClick: (songs: List<Song>, startIndex: Int) -> Unit = { _, _ -> },
) {
    val tabs = listOf(
        TabItem(stringResource(localesR.string.playlists), MuzIcons.Rounded.LibraryMusic),
        TabItem(stringResource(localesR.string.songs), MuzIcons.Rounded.MusicNote),
        TabItem(stringResource(localesR.string.albums), MuzIcons.Rounded.Album),
        TabItem(stringResource(localesR.string.artists), MuzIcons.Rounded.Artist),
    )
    var selectedTabIndex by remember { mutableIntStateOf(0) }

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
            Box {
                AnimatedContent(selectedTabIndex) { state ->
                    when (state) {
                        0 -> Unit
                        1 -> {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(300.dp),
                                contentPadding = PaddingValues(
                                    top = 8.dp,
                                    bottom = WindowInsets.navigationBars
                                        .asPaddingValues()
                                        .calculateBottomPadding(),
                                ),
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                items(songs) { song ->
                                    SongItem(
                                        song = song,
                                        modifier = Modifier.animateItem(),
                                        onClick = { onPlayClick(songs, songs.indexOf(song)) },
                                    )
                                }
                            }
                        }

                        2 -> Unit
                        3 -> Unit
                    }
                }

                AnimatedVisibility(
                    visible = libraryUiState.nowPlayingState.mediaId.isNotBlank(),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(16.dp),
                ) {
                    Surface(
                        tonalElevation = 2.dp,
                        shape = RoundedCornerShape(18.dp),
                    ) {
                        val currentSong = songs.find { it.mediaId == libraryUiState.nowPlayingState.mediaId }
                        currentSong?.let {
                            ListItem(
                                modifier = Modifier.fillMaxWidth(),
                                headlineContent = {
                                    Text(
                                        text = currentSong.title,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.basicMarquee(),
                                    )
                                },
                                supportingContent = {
                                    Text(
                                        text = currentSong.artist,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.basicMarquee(),
                                    )
                                },
                                leadingContent = {
                                    SubcomposeAsyncImage(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        model = currentSong.artworkUri,
                                        contentDescription = null,
                                        error = {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
                                            ) {
                                                Icon(
                                                    imageVector = MuzIcons.Rounded.MusicNote,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(24.dp),
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                )
                                            }
                                        },
                                        filterQuality = FilterQuality.Low,
                                    )
                                },
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