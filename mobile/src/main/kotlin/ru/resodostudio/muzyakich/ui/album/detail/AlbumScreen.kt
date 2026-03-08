package ru.resodostudio.muzyakich.ui.album.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledTonalIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Album
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowBack
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.ui.component.LoadingState
import ru.resodostudio.muzyakich.ui.component.songs
import ru.resodostudio.muzyakich.ui.component.songsInfo
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun AlbumScreen(
    onBackClick: () -> Unit,
    onSongMenuClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlbumViewModel = hiltViewModel(),
) {
    val albumUiState by viewModel.albumUiState.collectAsStateWithLifecycle()

    AlbumScreen(
        albumUiState = albumUiState,
        onBackClick = onBackClick,
        onSongMenuClick = onSongMenuClick,
        onPlaySongsClick = viewModel::playSongs,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AlbumScreen(
    albumUiState: AlbumUiState,
    onBackClick: () -> Unit,
    onSongMenuClick: (String) -> Unit,
    onPlaySongsClick: (List<Song>, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (albumUiState) {
        AlbumUiState.Error -> onBackClick()
        AlbumUiState.Loading -> LoadingState(modifier.fillMaxSize())
        is AlbumUiState.Success -> {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            val listState = rememberLazyGridState()
            val isScrolled by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
                }
            }

            Scaffold(
                topBar = {
                    AlbumTopAppBar(
                        title = albumUiState.album.title,
                        songs = albumUiState.album.songs,
                        isScrolled = isScrolled,
                        onBackClick = onBackClick,
                        scrollBehavior = scrollBehavior,
                    )
                },
                modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            ) { paddingValues ->
                LazyVerticalGrid(
                    state = listState,
                    columns = GridCells.Adaptive(300.dp),
                    contentPadding = PaddingValues(
                        bottom = 104.dp + paddingValues.calculateBottomPadding(),
                    ),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        val artworkUri = albumUiState.album.songs.firstOrNull()?.artworkUri
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(artworkUri)
                                .crossfade(true)
                                .placeholderMemoryCacheKey(artworkUri.toString())
                                .memoryCacheKey(artworkUri.toString())
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .drawWithCache {
                                    val brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.4f),
                                            Color.Transparent,
                                        ),
                                        endY = 120.dp.toPx(),
                                    )
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(brush)
                                    }
                                }
                                .clip(MaterialTheme.shapes.large),
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
                                        modifier = Modifier.fillMaxSize(0.5f),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            },
                        )
                    }
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
                                        start = 32.dp,
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
                                isPlaying = albumUiState.nowPlayingState.player?.isPlaying ?: false,
                                onSongMenuClick = onSongMenuClick,
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )
                        }
                    } else {
                        songs(
                            songs = albumUiState.album.songs,
                            currentMediaId = albumUiState.nowPlayingState.player?.currentMediaItem?.mediaId,
                            onPlaySongsClick = onPlaySongsClick,
                            isPlaying = albumUiState.nowPlayingState.player?.isPlaying ?: false,
                            onSongMenuClick = onSongMenuClick,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                    songsInfo(
                        songs = albumUiState.album.songs,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AlbumTopAppBar(
    title: String,
    songs: List<Song>,
    isScrolled: Boolean,
    onBackClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            AnimatedVisibility(
                visible = isScrolled,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.padding(start = 8.dp),
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        subtitle = {
            val firstYear = songs.firstOrNull()?.year ?: 0
            if (firstYear != 0 && songs.all { it.year == firstYear }) {
                AnimatedVisibility(
                    visible = isScrolled,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.padding(start = 8.dp),
                ) {
                    Text(
                        text = firstYear.toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        },
        navigationIcon = {
            MuzFilledTonalIconButton(
                icon = MuzIcons.Rounded.ArrowBack,
                onClick = onBackClick,
                contentDescription = stringResource(localesR.string.back),
                modifier = Modifier.padding(start = 8.dp),
                tooltipPosition = TooltipAnchorPosition.Right,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}
