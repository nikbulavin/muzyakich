package ru.resodostudio.muzyakich.feature.playlist.detail.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import ru.resodostudio.cashsense.core.ui.LoadingState
import ru.resodostudio.cashsense.core.ui.PlayShuffleButtonGroup
import ru.resodostudio.cashsense.core.ui.rememberPlaylistPlaySwipeAction
import ru.resodostudio.cashsense.core.ui.rememberRemoveFromPlaylistSwipeAction
import ru.resodostudio.cashsense.core.ui.songs
import ru.resodostudio.cashsense.core.ui.songsInfo
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledTonalIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Delete
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Edit
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.PlaylistPlay
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowBack
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.LibraryMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MoreVert
import ru.resodostudio.muzyakich.core.model.data.Playlist
import ru.resodostudio.muzyakich.core.model.data.Song
import kotlin.uuid.Uuid
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun PlaylistScreen(
    onBackClick: () -> Unit,
    onSongMenuClick: (String) -> Unit,
    onPlaylistEdit: (Uuid) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel = hiltViewModel(),
) {
    val playlistUiState by viewModel.playlistUiState.collectAsStateWithLifecycle()

    PlaylistScreen(
        playlistUiState = playlistUiState,
        onBackClick = onBackClick,
        onSongMenuClick = onSongMenuClick,
        onPlaylistEdit = onPlaylistEdit,
        onPlaySongsClick = viewModel::playSongs,
        onPlaySongsNextClick = viewModel::playSongsNext,
        onPlaylistDelete = viewModel::deletePlaylist,
        onSongLeftToRightSwipe = viewModel::playSongNext,
        onRemoveFromPlaylist = viewModel::removeSongFromPlaylist,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PlaylistScreen(
    playlistUiState: PlaylistUiState,
    onBackClick: () -> Unit,
    onSongMenuClick: (String) -> Unit,
    onPlaylistEdit: (Uuid) -> Unit,
    onPlaySongsClick: (List<Song>, Int, Boolean) -> Unit,
    onPlaySongsNextClick: (List<Song>) -> Unit,
    onPlaylistDelete: () -> Unit,
    modifier: Modifier = Modifier,
    onSongLeftToRightSwipe: (Song) -> Unit = {},
    onRemoveFromPlaylist: (Song) -> Unit = {},
) {
    when (playlistUiState) {
        PlaylistUiState.Error -> onBackClick()
        PlaylistUiState.Loading -> LoadingState(modifier.fillMaxSize())
        is PlaylistUiState.Success -> {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            val listState = rememberLazyGridState()
            val isScrolled by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 250
                }
            }

            Scaffold(
                topBar = {
                    PlaylistTopAppBar(
                        title = playlistUiState.playlist.title,
                        isScrolled = isScrolled,
                        songs = playlistUiState.playlist.songs,
                        onBackClick = onBackClick,
                        onPlaylistEdit = { onPlaylistEdit(playlistUiState.playlist.uuid) },
                        onPlaySongsNextClick = onPlaySongsNextClick,
                        onPlaylistDelete = onPlaylistDelete,
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
                        playlist = playlistUiState.playlist,
                    )
                    actionButtons(
                        onPlaySongsClick = { onPlaySongsClick(playlistUiState.playlist.songs, 0, false) },
                        onShuffleSongsClick = { onPlaySongsClick(playlistUiState.playlist.songs, 0, true) },
                    )
                    songs(
                        songs = playlistUiState.playlist.songs,
                        currentMediaId = playlistUiState.nowPlayingState.player?.currentMediaItem?.mediaId,
                        onPlaySongsClick = { songs, index ->
                            onPlaySongsClick(songs, index, false)
                        },
                        isPlaying = playlistUiState.nowPlayingState.player?.isPlaying ?: false,
                        onSongMenuClick = onSongMenuClick,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        startToEndSwipeAction = { song -> rememberPlaylistPlaySwipeAction(song, onSongLeftToRightSwipe) },
                        endToStartSwipeAction = { song -> rememberRemoveFromPlaylistSwipeAction(song, onRemoveFromPlaylist) },
                    )
                    songsInfo(
                        songs = playlistUiState.playlist.songs,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

private fun LazyGridScope.header(playlist: Playlist) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Column {
            val brushColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
            val artworkUri = playlist.coverFilePath
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(artworkUri)
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
                            imageVector = MuzIcons.Rounded.LibraryMusic,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(0.35f),
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
                    text = playlist.title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
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
        PlayShuffleButtonGroup(
            onPlayClick = onPlaySongsClick,
            onShuffleClick = onShuffleSongsClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            buttonSize = ButtonDefaults.MediumContainerHeight,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PlaylistTopAppBar(
    title: String,
    isScrolled: Boolean,
    songs: List<Song>,
    onBackClick: () -> Unit,
    onPlaylistEdit: () -> Unit,
    onPlaySongsNextClick: (List<Song>) -> Unit,
    onPlaylistDelete: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isScrolled) MaterialTheme.colorScheme.surface else Color.Transparent
    CenterAlignedTopAppBar(
        title = {
            AnimatedVisibility(
                visible = isScrolled,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        navigationIcon = {
            MuzFilledTonalIconButton(
                icon = MuzIcons.Rounded.ArrowBack,
                onClick = onBackClick,
                contentDescription = stringResource(localesR.string.core_locales_back),
                modifier = Modifier.padding(start = 8.dp),
                tooltipPosition = TooltipAnchorPosition.Right,
                colors = if (isScrolled) {
                    IconButtonDefaults.iconButtonVibrantColors()
                } else {
                    IconButtonDefaults.filledTonalIconButtonColors()
                },
                containerSize = IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow),
            )
        },
        actions = {
            PlaylistDropdownMenu(
                isScrolled = isScrolled,
                songs = songs,
                onPlaylistEdit = onPlaylistEdit,
                onPlaySongsNextClick = onPlaySongsNextClick,
                onPlaylistDelete = onPlaylistDelete,
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

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun PlaylistDropdownMenu(
    isScrolled: Boolean,
    songs: List<Song>,
    onPlaylistEdit: () -> Unit,
    onPlaySongsNextClick: (List<Song>) -> Unit,
    onPlaylistDelete: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    imageVector = MuzIcons.Filled.Delete,
                    contentDescription = null,
                )
            },
            title = {
                Text(
                    text = stringResource(localesR.string.core_locales_permanently_delete),
                    textAlign = TextAlign.Center,
                )
            },
            text = {
                Text(stringResource(localesR.string.core_locales_permanently_delete_playlist_description))
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onPlaylistDelete()
                    },
                    shapes = ButtonDefaults.shapes(),
                ) {
                    Text(stringResource(localesR.string.core_locales_delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    shapes = ButtonDefaults.shapes(),
                ) {
                    Text(stringResource(localesR.string.core_locales_cancel))
                }
            },
        )
    }

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopStart),
    ) {
        MuzFilledTonalIconButton(
            onClick = { expanded = true },
            icon = MuzIcons.Rounded.MoreVert,
            contentDescription = stringResource(localesR.string.core_locales_open_menu),
            modifier = Modifier
                .padding(end = 8.dp)
                .size(IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)),
            tooltipPosition = TooltipAnchorPosition.Left,
            colors = if (isScrolled) {
                IconButtonDefaults.iconButtonVibrantColors()
            } else {
                IconButtonDefaults.filledTonalIconButtonColors()
            },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = MenuDefaults.standaloneGroupShape,
            containerColor = MenuDefaults.groupVibrantContainerColor,
        ) {
            DropdownMenuItem(
                selected = false,
                text = { Text(stringResource(localesR.string.core_locales_play_next)) },
                shapes = MenuDefaults.itemShape(0, 3),
                leadingIcon = {
                    Icon(
                        imageVector = MuzIcons.Filled.PlaylistPlay,
                        modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                        contentDescription = null,
                    )
                },
                onClick = {
                    onPlaySongsNextClick(songs)
                    expanded = false
                },
                colors = MenuDefaults.selectableItemVibrantColors(),
            )
            DropdownMenuItem(
                selected = false,
                text = { Text(stringResource(localesR.string.core_locales_edit)) },
                shapes = MenuDefaults.itemShape(1, 3),
                leadingIcon = {
                    Icon(
                        imageVector = MuzIcons.Filled.Edit,
                        modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                        contentDescription = null,
                    )
                },
                onClick = {
                    onPlaylistEdit()
                    expanded = false
                },
                colors = MenuDefaults.selectableItemVibrantColors(),
            )
            DropdownMenuItem(
                selected = false,
                text = { Text(stringResource(localesR.string.core_locales_delete)) },
                shapes = MenuDefaults.itemShape(2, 3),
                leadingIcon = {
                    Icon(
                        imageVector = MuzIcons.Filled.Delete,
                        modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                        contentDescription = null,
                    )
                },
                onClick = {
                    showDeleteDialog = true
                    expanded = false
                },
                colors = MenuDefaults.selectableItemVibrantColors(),
            )
        }
    }
}
