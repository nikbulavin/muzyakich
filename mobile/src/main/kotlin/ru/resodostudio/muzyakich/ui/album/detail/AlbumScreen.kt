package ru.resodostudio.muzyakich.ui.album.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
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
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlayArrow
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Shuffle
import ru.resodostudio.muzyakich.core.model.data.Album
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
        onShuffleSongsClick = viewModel::shuffleSongs,
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
    onShuffleSongsClick: (List<Song>, Int) -> Unit,
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
                    listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 250
                }
            }

            Scaffold(
                topBar = {
                    AlbumTopAppBar(
                        title = albumUiState.album.title,
                        year = albumUiState.album.year,
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
                    header(
                        album = albumUiState.album,
                    )
                    actionButtons(
                        onPlaySongsClick = { onPlaySongsClick(albumUiState.album.songs, 0) },
                        onShuffleSongsClick = { onShuffleSongsClick(albumUiState.album.songs, 0) },
                    )

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

private fun LazyGridScope.header(album: Album) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Column {
            val brushColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
            val artworkUri = album.songs.firstOrNull()?.artworkUri
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
                            colors = listOf(brushColor, Color.Transparent),
                            endY = 150.dp.toPx(),
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = album.artist,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                val year = album.year
                if (year != null) {
                    Text(
                        text = year.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun LazyGridScope.actionButtons(
    onPlaySongsClick: () -> Unit,
    onShuffleSongsClick: () -> Unit,
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { "ActionButtons" },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val buttonSize = ButtonDefaults.MediumContainerHeight
            val buttonContentPadding = ButtonDefaults.contentPaddingFor(
                buttonHeight = buttonSize,
                hasStartIcon = true,
            )
            Button(
                shapes = ButtonDefaults.shapes(),
                onClick = onPlaySongsClick,
                modifier = Modifier
                    .heightIn(buttonSize)
                    .weight(1f),
                contentPadding = buttonContentPadding,
            ) {
                Icon(
                    imageVector = MuzIcons.Rounded.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.iconSizeFor(buttonSize)),
                )
                Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(buttonSize)))
                Text(
                    text = stringResource(localesR.string.play_audio),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = ButtonDefaults.textStyleFor(buttonSize),
                )
            }
            OutlinedButton(
                shapes = ButtonDefaults.shapes(),
                onClick = onShuffleSongsClick,
                modifier = Modifier
                    .heightIn(buttonSize)
                    .weight(1f),
                contentPadding = buttonContentPadding,
            ) {
                Icon(
                    imageVector = MuzIcons.Rounded.Shuffle,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.iconSizeFor(buttonSize)),
                )
                Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(buttonSize)))
                Text(
                    text = stringResource(localesR.string.shuffle),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = ButtonDefaults.textStyleFor(buttonSize),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AlbumTopAppBar(
    title: String,
    year: Int?,
    isScrolled: Boolean,
    onBackClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isScrolled) MaterialTheme.colorScheme.surface else Color.Transparent
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
            if (year != null) {
                AnimatedVisibility(
                    visible = isScrolled,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.padding(start = 8.dp),
                ) {
                    Text(
                        text = year.toString(),
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
            containerColor = containerColor,
            scrolledContainerColor = containerColor,
        ),
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}
