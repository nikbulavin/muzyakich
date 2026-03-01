package ru.resodostudio.muzyakich.ui.album.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowBack
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.ui.component.LoadingState
import ru.resodostudio.muzyakich.ui.component.songs
import ru.resodostudio.muzyakich.ui.component.songsInfo
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun AlbumScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlbumViewModel = hiltViewModel(),
) {
    val albumUiState by viewModel.albumUiState.collectAsStateWithLifecycle()

    AlbumScreen(
        albumUiState = albumUiState,
        onBackClick = onBackClick,
        onPlaySongsClick = viewModel::playSongs,
        onPlayNextClick = viewModel::playSongNext,
        onFavoriteChange = viewModel::setSongFavorite,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AlbumScreen(
    albumUiState: AlbumUiState,
    onBackClick: () -> Unit,
    onPlaySongsClick: (List<Song>, Int) -> Unit,
    onPlayNextClick: (Song) -> Unit,
    onFavoriteChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (albumUiState) {
        AlbumUiState.Error -> LoadingState(modifier.fillMaxSize())
        AlbumUiState.Loading -> LoadingState(modifier.fillMaxSize())
        is AlbumUiState.Success -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = albumUiState.album.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        subtitle = {
                            val firstYear = albumUiState.album.songs.firstOrNull()?.year ?: 0
                            if (firstYear != 0 && albumUiState.album.songs.all { it.year == firstYear }) {
                                Text(
                                    text = firstYear.toString(),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = MuzIcons.Rounded.ArrowBack,
                                    contentDescription = null,
                                )
                            }
                        },
                    )
                },
                modifier = modifier,
            ) { paddingValues ->
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(300.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = paddingValues.calculateTopPadding(),
                        bottom = 104.dp + paddingValues.calculateBottomPadding(),
                    ),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
                ) {
                    val groupedSongs = albumUiState.album.songs.groupBy { it.trackNumber / 1000 }
                    val hasMultipleDiscs = albumUiState.album.songs.isNotEmpty() &&
                            albumUiState.album.songs.all { it.trackNumber >= 1000 } &&
                            groupedSongs.size > 1

                    if (hasMultipleDiscs) {
                        groupedSongs.forEach { (discNumber, groupSongs) ->
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Text(
                                    text = stringResource(localesR.string.disc_number, discNumber),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 16.dp,
                                        bottom = 8.dp,
                                    ),
                                )
                            }
                            songs(
                                songs = groupSongs,
                                currentMediaId = albumUiState.nowPlayingState.player?.currentMediaItem?.mediaId,
                                onPlaySongsClick = { _, index ->
                                    val songToPlay = groupSongs[index]
                                    onPlaySongsClick(
                                        albumUiState.album.songs,
                                        albumUiState.album.songs.indexOf(songToPlay),
                                    )
                                },
                                onPlayNextClick = onPlayNextClick,
                                isPlaying = albumUiState.nowPlayingState.player?.isPlaying ?: false,
                                onFavoriteChange = onFavoriteChange,
                            )
                        }
                    } else {
                        songs(
                            songs = albumUiState.album.songs,
                            currentMediaId = albumUiState.nowPlayingState.player?.currentMediaItem?.mediaId,
                            onPlaySongsClick = onPlaySongsClick,
                            onPlayNextClick = onPlayNextClick,
                            isPlaying = albumUiState.nowPlayingState.player?.isPlaying ?: false,
                            onFavoriteChange = onFavoriteChange,
                        )
                    }
                    songsInfo(
                        songs = albumUiState.album.songs,
                    )
                }
            }
        }
    }
}