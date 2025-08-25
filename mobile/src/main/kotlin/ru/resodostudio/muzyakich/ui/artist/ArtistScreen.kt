package ru.resodostudio.muzyakich.ui.artist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowBack
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.ui.component.LoadingState
import ru.resodostudio.muzyakich.ui.component.songs
import ru.resodostudio.muzyakich.ui.component.songsInfo

@Composable
fun ArtistScreen(
    onBackClick: () -> Unit,
    viewModel: ArtistViewModel = hiltViewModel(),
) {
    val artistUiState = viewModel.artistUiState.collectAsStateWithLifecycle()

    ArtistScreen(
        artistUiState = artistUiState.value,
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(
    artistUiState: ArtistUiState,
    onBackClick: () -> Unit,
) {
    when (artistUiState) {
        ArtistUiState.Error -> LoadingState()
        ArtistUiState.Loading -> LoadingState()
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
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = MuzIcons.Rounded.ArrowBack,
                                    contentDescription = null,
                                )
                            }
                        },
                    )
                }
            ) { paddingValues ->
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(300.dp),
                    contentPadding = PaddingValues(
                        top = paddingValues.calculateTopPadding() + 8.dp,
                        bottom = 104.dp + paddingValues.calculateBottomPadding(),
                    ),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    songs(
                        songs = artistUiState.artist.songs,
                        nowPlayingState = NowPlayingState(),
                        onPlaySongsClick = { _, _ -> },
                        onPlayNextClick = {},
                    )
                    songsInfo(
                        songs = artistUiState.artist.songs,
                    )
                }
            }
        }
    }
}