package ru.resodostudio.muzyakich.ui.artist.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowBack
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.ui.component.LoadingState
import ru.resodostudio.muzyakich.ui.component.songs
import ru.resodostudio.muzyakich.ui.component.songsInfo
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun ArtistScreen(
    onBackClick: () -> Unit,
    onSongMenuClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ArtistViewModel = hiltViewModel(),
) {
    val artistUiState = viewModel.artistUiState.collectAsStateWithLifecycle()

    ArtistScreen(
        artistUiState = artistUiState.value,
        onBackClick = onBackClick,
        onSongMenuClick = onSongMenuClick,
        modifier = modifier,
        onPlaySongsClick = viewModel::playSongs,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ArtistScreen(
    artistUiState: ArtistUiState,
    onBackClick: () -> Unit,
    onSongMenuClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onPlaySongsClick: (songs: List<Song>, startIndex: Int, shuffle: Boolean) -> Unit = { _, _, _ -> },
) {
    when (artistUiState) {
        ArtistUiState.Error -> LoadingState(modifier.fillMaxSize())
        ArtistUiState.Loading -> LoadingState(modifier.fillMaxSize())
        is ArtistUiState.Success -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = artistUiState.artist.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        navigationIcon = {
                            MuzIconButton(
                                onClick = onBackClick,
                                icon = MuzIcons.Rounded.ArrowBack,
                                contentDescription = stringResource(localesR.string.back),
                            )
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
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    songs(
                        songs = artistUiState.artist.songs,
                        currentMediaId = artistUiState.nowPlayingState.player?.currentMediaItem?.mediaId,
                        onPlaySongsClick = { songs, index -> onPlaySongsClick(songs, index, false) },
                        isPlaying = artistUiState.nowPlayingState.player?.isPlaying ?: false,
                        onSongMenuClick = onSongMenuClick,
                    )
                    songsInfo(
                        songs = artistUiState.artist.songs,
                    )
                }
            }
        }
    }
}