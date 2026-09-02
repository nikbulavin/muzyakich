package ru.resodostudio.muzyakich.feature.playlist.detail.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.snap
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledTonalIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Delete
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Edit
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.PlaylistPlay
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowBack
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.LibraryMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MoreVert
import ru.resodostudio.muzyakich.core.designsystem.theme.DynamicMuzTheme
import ru.resodostudio.muzyakich.core.designsystem.theme.LocalSharedTransitionScope
import ru.resodostudio.muzyakich.core.designsystem.theme.SharedElementKey
import ru.resodostudio.muzyakich.core.designsystem.theme.SharedElementType
import ru.resodostudio.muzyakich.core.designsystem.theme.sharedElementTransitionSpec
import ru.resodostudio.muzyakich.core.model.Playlist
import ru.resodostudio.muzyakich.core.model.PlaylistSong
import ru.resodostudio.muzyakich.core.model.Song
import ru.resodostudio.muzyakich.core.ui.DeleteConfirmationDialog
import ru.resodostudio.muzyakich.core.ui.LoadingState
import ru.resodostudio.muzyakich.core.ui.PlayShuffleButtonGroup
import ru.resodostudio.muzyakich.core.ui.SongItem
import ru.resodostudio.muzyakich.core.ui.SwipeAction
import ru.resodostudio.muzyakich.core.ui.rememberPlaylistPlaySwipeAction
import ru.resodostudio.muzyakich.core.ui.rememberRemoveFromPlaylistSwipeAction
import ru.resodostudio.muzyakich.core.ui.songsInfo
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

@Composable
private fun PlaylistScreen(
    playlistUiState: PlaylistUiState,
    onBackClick: () -> Unit,
    onSongMenuClick: (String) -> Unit,
    onPlaylistEdit: (Uuid) -> Unit,
    onPlaySongsClick: (List<PlaylistSong>, Int, Boolean) -> Unit,
    onPlaySongsNextClick: (List<PlaylistSong>) -> Unit,
    onPlaylistDelete: () -> Unit,
    modifier: Modifier = Modifier,
    onSongLeftToRightSwipe: (Song) -> Unit = {},
    onRemoveFromPlaylist: (Uuid) -> Unit = {},
) {
    with(LocalSharedTransitionScope.current) {
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

                var songToRemove by remember { mutableStateOf<Uuid?>(null) }

                songToRemove?.let { playlistSongUuid ->
                    DeleteConfirmationDialog(
                        title = stringResource(localesR.string.core_locales_remove_from_playlist),
                        text = stringResource(localesR.string.core_locales_remove_song_from_playlist_description),
                        confirmButtonText = stringResource(localesR.string.core_locales_delete),
                        onConfirm = { onRemoveFromPlaylist(playlistSongUuid) },
                        onDismissRequest = { songToRemove = null },
                    )
                }

                DynamicMuzTheme(
                    artworkUri = playlistUiState.playlist.coverFilePath,
                ) {
                    Scaffold(
                        topBar = {
                            PlaylistTopAppBar(
                                title = playlistUiState.playlist.title,
                                isScrolled = isScrolled,
                                onBackClick = onBackClick,
                                onPlaylistEdit = { onPlaylistEdit(playlistUiState.playlist.uuid) },
                                onPlaySongsNextClick = { onPlaySongsNextClick(playlistUiState.playlist.songs) },
                                onPlaylistDelete = onPlaylistDelete,
                                scrollBehavior = scrollBehavior,
                            )
                        },
                        modifier = modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(
                                    key = SharedElementKey(
                                        id = playlistUiState.playlist.uuid.toString(),
                                        origin = playlistUiState.playlist.uuid.toString(),
                                        type = SharedElementType.Bounds,
                                    ),
                                ),
                                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                placeholderSize = SharedTransitionScope.PlaceholderSize.AnimatedSize,
                            )
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
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
                                onPlaySongsClick = {
                                    onPlaySongsClick(playlistUiState.playlist.songs, 0, false)
                                },
                                onShuffleSongsClick = {
                                    onPlaySongsClick(playlistUiState.playlist.songs, 0, true)
                                },
                                enabled = playlistUiState.playlist.songs.isNotEmpty(),
                            )
                            songs(
                                playlistSongs = playlistUiState.playlist.songs,
                                currentMediaId = playlistUiState.nowPlayingState.mediaId,
                                onPlaySongsClick = { songs, index ->
                                    onPlaySongsClick(songs, index, false)
                                },
                                isPlaying = playlistUiState.nowPlayingState.isPlaying,
                                onSongMenuClick = onSongMenuClick,
                                modifier = Modifier.padding(horizontal = 16.dp),
                                startToEndSwipeAction = { song ->
                                    rememberPlaylistPlaySwipeAction(
                                        song = song,
                                        onSwipe = onSongLeftToRightSwipe,
                                    )
                                },
                                endToStartSwipeAction = { playlistSongUuid ->
                                    rememberRemoveFromPlaylistSwipeAction {
                                        songToRemove = playlistSongUuid
                                    }
                                },
                            )
                            songsInfo(
                                songs = playlistUiState.playlist.songs.map { it.song },
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun LazyGridScope.header(playlist: Playlist) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        with(LocalSharedTransitionScope.current) {
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
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(
                                key = SharedElementKey(
                                    id = playlist.uuid.toString(),
                                    origin = artworkUri.toString(),
                                    type = SharedElementType.Artwork,
                                ),
                            ),
                            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                            boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                        )
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
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(
                                    key = SharedElementKey(
                                        id = playlist.uuid.toString(),
                                        origin = playlist.title,
                                        type = SharedElementType.Title,
                                    ),
                                ),
                                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                            ),
                    )
                }
            }
        }
    }
}

private fun LazyGridScope.actionButtons(
    onPlaySongsClick: () -> Unit,
    onShuffleSongsClick: () -> Unit,
    enabled: Boolean = true,
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
            enabled = enabled,
        )
    }
}

fun LazyGridScope.songs(
    playlistSongs: List<PlaylistSong>,
    currentMediaId: String?,
    onPlaySongsClick: (List<PlaylistSong>, Int) -> Unit,
    onSongMenuClick: (String) -> Unit,
    isPlaying: Boolean = false,
    modifier: Modifier = Modifier,
    startToEndSwipeAction: @Composable ((Song) -> SwipeAction?)? = null,
    endToStartSwipeAction: @Composable ((Uuid) -> SwipeAction?)? = null,
) {
    itemsIndexed(
        items = playlistSongs,
        key = { _, playlistSong -> playlistSong.uuid },
        contentType = { _, _ -> "Song" },
    ) { index, playlistSong ->
        SongItem(
            song = playlistSong.song,
            isPlaying = currentMediaId == playlistSong.song.mediaId && isPlaying,
            modifier = modifier.animateItem(),
            onClick = { onPlaySongsClick(playlistSongs, playlistSongs.indexOf(playlistSong)) },
            onMenuClick = { onSongMenuClick(playlistSong.song.mediaId) },
            shapes = ListItemDefaults.segmentedShapes(index, playlistSongs.size),
            startToEndSwipeAction = startToEndSwipeAction?.invoke(playlistSong.song),
            endToStartSwipeAction = endToStartSwipeAction?.invoke(playlistSong.uuid),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaylistTopAppBar(
    title: String,
    isScrolled: Boolean,
    onBackClick: () -> Unit,
    onPlaylistEdit: () -> Unit,
    onPlaySongsNextClick: () -> Unit,
    onPlaylistDelete: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isScrolled) MaterialTheme.colorScheme.surface else Color.Transparent
    with(LocalNavAnimatedContentScope.current) {
        with(LocalSharedTransitionScope.current) {
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
                modifier = modifier
                    .renderInSharedTransitionScopeOverlay(1f)
                    .animateEnterExit(
                        exit = fadeOut(snap()),
                    ),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaylistDropdownMenu(
    isScrolled: Boolean,
    onPlaylistEdit: () -> Unit,
    onPlaySongsNextClick: () -> Unit,
    onPlaylistDelete: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            title = stringResource(localesR.string.core_locales_permanently_delete),
            text = stringResource(localesR.string.core_locales_permanently_delete_playlist_description),
            onConfirm = onPlaylistDelete,
            onDismissRequest = { showDeleteDialog = false },
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
                text = { Text(stringResource(localesR.string.core_locales_play_next)) },
                shape = MenuDefaults.leadingItemShape,
                leadingIcon = {
                    Icon(
                        imageVector = MuzIcons.Filled.PlaylistPlay,
                        modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                        contentDescription = null,
                    )
                },
                onClick = {
                    onPlaySongsNextClick()
                    expanded = false
                },
                colors = MenuDefaults.itemVibrantColors(),
            )
            DropdownMenuItem(
                text = { Text(stringResource(localesR.string.core_locales_edit)) },
                shape = MenuDefaults.middleItemShape,
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
                colors = MenuDefaults.itemVibrantColors(),
            )
            DropdownMenuItem(
                text = { Text(stringResource(localesR.string.core_locales_delete)) },
                shape = MenuDefaults.trailingItemShape,
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
                colors = MenuDefaults.itemVibrantColors(),
            )
        }
    }
}
