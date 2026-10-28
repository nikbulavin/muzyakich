package ru.resodostudio.muzyakich.feature.album.detail.impl

import android.app.Activity.RESULT_OK
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledTonalIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.AutoDelete
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.PlaylistPlay
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Album
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowBack
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MoreVert
import ru.resodostudio.muzyakich.core.designsystem.theme.DynamicMuzTheme
import ru.resodostudio.muzyakich.core.designsystem.theme.LocalSharedTransitionScope
import ru.resodostudio.muzyakich.core.designsystem.theme.SharedElementKey
import ru.resodostudio.muzyakich.core.designsystem.theme.SharedElementType
import ru.resodostudio.muzyakich.core.designsystem.theme.sharedElementTransitionSpec
import ru.resodostudio.muzyakich.core.model.Album
import ru.resodostudio.muzyakich.core.model.Song
import ru.resodostudio.muzyakich.core.ui.ArtworkBackground
import ru.resodostudio.muzyakich.core.ui.LoadingState
import ru.resodostudio.muzyakich.core.ui.PlayShuffleButtonGroup
import ru.resodostudio.muzyakich.core.ui.rememberDeleteSwipeAction
import ru.resodostudio.muzyakich.core.ui.rememberPlaylistPlaySwipeAction
import ru.resodostudio.muzyakich.core.ui.songs
import ru.resodostudio.muzyakich.core.ui.songsInfo
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun AlbumScreen(
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
        onPlaySongsNextClick = viewModel::playSongsNext,
        onRemoveSongsClick = viewModel::removeSongs,
        onSongLeftToRightSwipe = viewModel::playSongNext,
        onSongRemove = { viewModel.removeSongs(listOf(it)) },
        modifier = modifier,
    )
}

@Composable
private fun AlbumScreen(
    albumUiState: AlbumUiState,
    onBackClick: () -> Unit,
    onSongMenuClick: (String) -> Unit,
    onPlaySongsClick: (List<Song>, Int, Boolean) -> Unit,
    onPlaySongsNextClick: (List<Song>) -> Unit,
    onRemoveSongsClick: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    onSongLeftToRightSwipe: (Song) -> Unit = {},
    onSongRemove: (String) -> Unit = {},
) {
    when (albumUiState) {
        AlbumUiState.Error -> onBackClick()
        AlbumUiState.Loading -> LoadingState(modifier.fillMaxSize())
        is AlbumUiState.Success -> {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
            val listState = rememberLazyGridState()
            val isScrolled by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 850
                }
            }
            val songs = albumUiState.album.songs
            val artworkUri = songs.firstOrNull()?.artworkUri

            val groupedSongs = remember(songs) {
                songs.groupBy { it.trackNumber / 1000 }
            }
            val hasMultipleDiscs = remember(songs, groupedSongs) {
                songs.isNotEmpty() &&
                        songs.all { it.trackNumber >= 1000 } &&
                        groupedSongs.size > 1
            }

            DynamicMuzTheme(
                artworkUri = artworkUri,
            ) {
                with(LocalSharedTransitionScope.current) {
                    Scaffold(
                        topBar = {
                            AlbumTopAppBar(
                                title = albumUiState.album.title,
                                isScrolled = isScrolled,
                                songs = songs,
                                onBackClick = onBackClick,
                                onPlaySongsNextClick = onPlaySongsNextClick,
                                onRemoveSongsClick = onRemoveSongsClick,
                                scrollBehavior = scrollBehavior,
                            )
                        },
                        modifier = modifier
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(
                                    key = SharedElementKey(
                                        id = albumUiState.album.id.toString(),
                                        origin = albumUiState.album.id.toString(),
                                        type = SharedElementType.Bounds,
                                    ),
                                ),
                                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                placeholderSize = SharedTransitionScope.PlaceholderSize.AnimatedSize,
                            ),
                    ) { paddingValues ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            ArtworkBackground(
                                artworkUri = artworkUri,
                                sharedElementId = albumUiState.album.id.toString(),
                                placeholderIcon = MuzIcons.Rounded.Album,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer {
                                        val isHeaderVisible = listState.firstVisibleItemIndex == 0
                                        translationY = if (isHeaderVisible) {
                                            -listState.firstVisibleItemScrollOffset.toFloat()
                                        } else {
                                            -10000f
                                        }
                                        alpha = if (isHeaderVisible) 1f else 0f
                                    },
                            )
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
                                    onPlaySongsClick = {
                                        onPlaySongsClick(
                                            songs,
                                            0,
                                            false,
                                        )
                                    },
                                    onShuffleSongsClick = {
                                        onPlaySongsClick(
                                            songs,
                                            0,
                                            true,
                                        )
                                    },
                                )
                                groupedSongs(
                                    songs = songs,
                                    groupedSongs = groupedSongs,
                                    hasMultipleDiscs = hasMultipleDiscs,
                                    currentMediaId = albumUiState.nowPlayingState.mediaId,
                                    isPlaying = albumUiState.nowPlayingState.isPlaying,
                                    onPlaySongsClick = { songsToPlay, index ->
                                        onPlaySongsClick(songsToPlay, index, false)
                                    },
                                    onSongMenuClick = onSongMenuClick,
                                    onSongLeftToRightSwipe = onSongLeftToRightSwipe,
                                    onSongRemove = onSongRemove,
                                )
                                songsInfo(
                                    songs = songs,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun LazyGridScope.header(album: Album) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        with(LocalSharedTransitionScope.current) {
            Column {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = album.title,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
                    val labels = listOfNotNull(album.genre, album.year)
                    if (labels.isNotEmpty()) {
                        Text(
                            text = labels.joinToString(" • "),
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
}

private fun LazyGridScope.groupedSongs(
    songs: List<Song>,
    groupedSongs: Map<Int, List<Song>>,
    hasMultipleDiscs: Boolean,
    currentMediaId: String?,
    isPlaying: Boolean,
    onPlaySongsClick: (List<Song>, Int) -> Unit,
    onSongMenuClick: (String) -> Unit,
    onSongLeftToRightSwipe: (Song) -> Unit = {},
    onSongRemove: (String) -> Unit = {},
) {
    if (hasMultipleDiscs) {
        groupedSongs.forEach { (discNumber, groupSongs) ->
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = stringResource(localesR.string.core_locales_disc_number, discNumber),
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
                currentMediaId = currentMediaId,
                onPlaySongsClick = { _, index ->
                    val songToPlay = groupSongs[index]
                    onPlaySongsClick(songs, songs.indexOf(songToPlay))
                },
                isPlaying = isPlaying,
                onSongMenuClick = onSongMenuClick,
                modifier = Modifier.padding(horizontal = 16.dp),
                startToEndSwipeAction = { song ->
                    rememberPlaylistPlaySwipeAction(
                        song,
                        onSongLeftToRightSwipe,
                    )
                },
                endToStartSwipeAction = { song -> rememberDeleteSwipeAction(song, onSongRemove) },
                shouldShowArtworks = false,
            )
        }
    } else {
        songs(
            songs = songs,
            currentMediaId = currentMediaId,
            onPlaySongsClick = onPlaySongsClick,
            isPlaying = isPlaying,
            onSongMenuClick = onSongMenuClick,
            modifier = Modifier.padding(horizontal = 16.dp),
            startToEndSwipeAction = { song ->
                rememberPlaylistPlaySwipeAction(
                    song,
                    onSongLeftToRightSwipe,
                )
            },
            endToStartSwipeAction = { song -> rememberDeleteSwipeAction(song, onSongRemove) },
            shouldShowArtworks = false,
        )
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumTopAppBar(
    title: String,
    isScrolled: Boolean,
    songs: List<Song>,
    onBackClick: () -> Unit,
    onPlaySongsNextClick: (List<Song>) -> Unit,
    onRemoveSongsClick: (List<String>) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    with(LocalNavAnimatedContentScope.current) {
        with(LocalSharedTransitionScope.current) {
            val containerColor =
                if (isScrolled) MaterialTheme.colorScheme.surface else Color.Transparent
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
                    AlbumDropdownMenu(
                        isScrolled = isScrolled,
                        songs = songs,
                        onPlaySongsNextClick = onPlaySongsNextClick,
                        onRemoveSongsClick = onRemoveSongsClick,
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
private fun AlbumDropdownMenu(
    isScrolled: Boolean,
    songs: List<Song>,
    onPlaySongsNextClick: (List<Song>) -> Unit,
    onRemoveSongsClick: (List<String>) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
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
                    onPlaySongsNextClick(songs)
                    expanded = false
                },
                colors = MenuDefaults.itemVibrantColors(),
            )
            val context = LocalContext.current
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
            ) { result ->
                if (result.resultCode == RESULT_OK) onRemoveSongsClick(songs.map { it.mediaId })
            }
            DropdownMenuItem(
                text = { Text(stringResource(localesR.string.core_locales_move_to_trash)) },
                shape = MenuDefaults.trailingItemShape,
                leadingIcon = {
                    Icon(
                        imageVector = MuzIcons.Filled.AutoDelete,
                        modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                        contentDescription = null,
                    )
                },
                onClick = {
                    runCatching {
                        val pendingIntent = MediaStore.createTrashRequest(
                            context.contentResolver,
                            songs.map { it.mediaUri.toUri() },
                            true,
                        )
                        launcher.launch(
                            IntentSenderRequest.Builder(pendingIntent.intentSender).build(),
                        )
                    }
                },
                colors = MenuDefaults.itemVibrantColors(),
            )
        }
    }
}
