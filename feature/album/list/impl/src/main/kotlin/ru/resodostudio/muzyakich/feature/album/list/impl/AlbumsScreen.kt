package ru.resodostudio.muzyakich.feature.album.list.impl

import androidx.compose.animation.SharedTransitionScope
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
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import coil3.compose.SubcomposeAsyncImage
import ru.resodostudio.cashsense.core.ui.EmptyState
import ru.resodostudio.cashsense.core.ui.LoadingState
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Album
import ru.resodostudio.muzyakich.core.designsystem.theme.LocalSharedTransitionScope
import ru.resodostudio.muzyakich.core.designsystem.theme.SharedElementKey
import ru.resodostudio.muzyakich.core.designsystem.theme.SharedElementType
import ru.resodostudio.muzyakich.core.designsystem.theme.sharedElementTransitionSpec
import ru.resodostudio.muzyakich.core.model.Album
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun AlbumsScreen(
    onAlbumClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlbumsViewModel = hiltViewModel(),
) {
    val albumsUiState by viewModel.albumsUiState.collectAsStateWithLifecycle()

    AlbumsScreen(
        albumsUiState = albumsUiState,
        onAlbumClick = onAlbumClick,
        modifier = modifier,
    )
}

@Composable
private fun AlbumsScreen(
    albumsUiState: AlbumsUiState,
    onAlbumClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (albumsUiState) {
        AlbumsUiState.Empty -> {
            EmptyState(
                messageRes = localesR.string.core_locales_library_empty,
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp)
                    .navigationBarsPadding(),
            )
        }

        AlbumsUiState.Loading -> {
            LoadingState(
                modifier = modifier
                    .fillMaxSize()
                    .navigationBarsPadding(),
            )
        }

        is AlbumsUiState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                modifier = Modifier.fillMaxSize(),
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
                albums(
                    albums = albumsUiState.albums,
                    onAlbumClick = onAlbumClick,
                )
            }
        }
    }
}

private fun LazyGridScope.albums(
    albums: List<Album>,
    onAlbumClick: (Long) -> Unit,
) {
    items(
        items = albums,
        key = { album -> album.id },
        contentType = { _ -> "Album" },
    ) { album ->
        AlbumCard(
            album = album,
            onClick = onAlbumClick,
            modifier = Modifier.animateItem(),
        )
    }
}

@Composable
private fun AlbumCard(
    album: Album,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    with(LocalSharedTransitionScope.current) {
        OutlinedCard(
            onClick = dropUnlessResumed { onClick(album.id) },
            modifier = modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        key = SharedElementKey(
                            id = album.id.toString(),
                            origin = album.id.toString(),
                            type = SharedElementType.Bounds,
                        ),
                    ),
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                    placeholderSize = SharedTransitionScope.PlaceholderSize.AnimatedSize,
                ),
        ) {
            Column {
                val artworkUri = album.songs.firstOrNull()?.artworkUri
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(
                                key = SharedElementKey(
                                    id = album.id.toString(),
                                    origin = artworkUri.toString(),
                                    type = SharedElementType.Artwork,
                                ),
                            ),
                            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                            boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                        )
                        .clip(MaterialTheme.shapes.medium),
                    model = artworkUri,
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
                                imageVector = MuzIcons.Rounded.Album,
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
                        text = album.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .sharedBounds(
                                rememberSharedContentState(
                                    key = SharedElementKey(
                                        id = album.id.toString(),
                                        origin = album.title,
                                        type = SharedElementType.Title,
                                    ),
                                ),
                                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                                boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                            ),
                    )
                    Text(
                        text = album.artist,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .sharedBounds(
                                rememberSharedContentState(
                                    key = SharedElementKey(
                                        id = album.id.toString(),
                                        origin = album.artist,
                                        type = SharedElementType.Artist,
                                    ),
                                ),
                                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                                boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                            ),
                    )
                }
            }
        }
    }
}
