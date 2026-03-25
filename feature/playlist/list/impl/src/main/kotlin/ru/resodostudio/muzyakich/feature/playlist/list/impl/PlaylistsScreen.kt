package ru.resodostudio.muzyakich.feature.playlist.list.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import coil3.compose.SubcomposeAsyncImage
import ru.resodostudio.cashsense.core.ui.EmptyState
import ru.resodostudio.cashsense.core.ui.LoadingState
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.LibraryMusic
import ru.resodostudio.muzyakich.core.model.data.Playlist
import kotlin.uuid.Uuid
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun PlaylistsScreen(
    onPlaylistClick: (Uuid) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaylistsViewModel = hiltViewModel(),
) {
    val playlistsUiState by viewModel.playlistsUiState.collectAsStateWithLifecycle()

    PlaylistsScreen(
        playlistsUiState = playlistsUiState,
        onPlaylistClick = onPlaylistClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PlaylistsScreen(
    playlistsUiState: PlaylistsUiState,
    onPlaylistClick: (Uuid) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (playlistsUiState) {
        PlaylistsUiState.Empty -> {
            EmptyState(
                messageRes = localesR.string.core_locales_playlists_empty,
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp)
                    .navigationBarsPadding(),
            )
        }

        PlaylistsUiState.Loading -> {
            LoadingState(
                modifier = modifier
                    .fillMaxSize()
                    .navigationBarsPadding(),
            )
        }

        is PlaylistsUiState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 104.dp + WindowInsets.navigationBars.asPaddingValues()
                        .calculateBottomPadding(),
                ),
            ) {
                playlists(
                    playlists = playlistsUiState.playlists,
                    onPlaylistClick = onPlaylistClick,
                )
            }
        }
    }
}

private fun LazyGridScope.playlists(
    playlists: List<Playlist>,
    onPlaylistClick: (Uuid) -> Unit,
) {
    items(
        items = playlists,
        key = { playlist -> playlist.uuid.toString() },
        contentType = { _ -> "Playlist" },
    ) { playlist ->
        PlaylistCard(
            playlist = playlist,
            onClick = onPlaylistClick,
            modifier = Modifier.animateItem(),
        )
    }
}

@Composable
private fun PlaylistCard(
    playlist: Playlist,
    onClick: (Uuid) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        onClick = dropUnlessResumed { onClick(playlist.uuid) },
        modifier = modifier,
    ) {
        Column {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium),
                model = playlist.coverFilePath,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                error = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                    ) {
                        Icon(
                            imageVector = MuzIcons.Rounded.LibraryMusic,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(0.35f),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )
            Column(
                modifier = Modifier.padding(12.dp),
            ) {
                Text(
                    text = playlist.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
