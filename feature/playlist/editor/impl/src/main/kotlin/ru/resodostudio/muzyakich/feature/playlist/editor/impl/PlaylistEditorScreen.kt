package ru.resodostudio.muzyakich.feature.playlist.editor.impl

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import ru.resodostudio.cashsense.core.ui.LoadingState
import ru.resodostudio.cashsense.core.ui.SwipeableItem
import ru.resodostudio.cashsense.core.ui.rememberRemoveFromPlaylistSwipeAction
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledIconButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledTonalIconButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSelectableListItem
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Delete
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Edit
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowBack
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Check
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.DragHandle
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.LibraryMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.feature.song.picker.SongPickerBottomSheet
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun PlaylistEditorScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaylistEditorViewModel = hiltViewModel(),
) {
    val playlistEditorUiState by viewModel.playlistEditorUiState.collectAsStateWithLifecycle()

    PlaylistEditorScreen(
        playlistEditorUiState = playlistEditorUiState,
        onBackClick = onBackClick,
        onTitleChange = viewModel::onTitleChange,
        onCoverSelected = viewModel::updateCover,
        onRemoveCover = viewModel::removeCover,
        onAddSongs = viewModel::addSongs,
        onRemoveSong = viewModel::removeSong,
        onReorderSongs = viewModel::reorderSongs,
        onSave = {
            viewModel.savePlaylist()
            onBackClick()
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PlaylistEditorScreen(
    playlistEditorUiState: PlaylistEditorUiState,
    onBackClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onCoverSelected: (Uri?) -> Unit,
    onRemoveCover: () -> Unit,
    onAddSongs: (List<Song>) -> Unit,
    onRemoveSong: (Song) -> Unit,
    onReorderSongs: (Int, Int) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = onCoverSelected,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    MuzIconButton(
                        icon = MuzIcons.Rounded.ArrowBack,
                        onClick = onBackClick,
                        contentDescription = stringResource(localesR.string.core_locales_back),
                        tooltipPosition = TooltipAnchorPosition.Right,
                    )
                },
                actions = {
                    MuzFilledIconButton(
                        icon = MuzIcons.Rounded.Check,
                        onClick = onSave,
                        contentDescription = stringResource(localesR.string.core_locales_confirm),
                        tooltipPosition = TooltipAnchorPosition.Left,
                        enabled = playlistEditorUiState is PlaylistEditorUiState.Success && playlistEditorUiState.title.isNotBlank(),
                        containerSize = IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide),
                        modifier = Modifier.padding(end = 6.dp),
                    )
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        when (playlistEditorUiState) {
            PlaylistEditorUiState.Loading -> {
                LoadingState(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                )
            }

            is PlaylistEditorUiState.Success -> {
                val lazyListState = rememberLazyListState()
                val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
                    val fromKey = from.key as? String
                    val toKey = to.key as? String
                    if (fromKey != null && toKey != null) {
                        val fromIndex = playlistEditorUiState.songs.indexOfFirst { it.mediaId == fromKey }
                        val toIndex = playlistEditorUiState.songs.indexOfFirst { it.mediaId == toKey }
                        if (fromIndex != -1 && toIndex != -1) onReorderSongs(fromIndex, toIndex)
                    }
                }
                
                val arrangementPadding = 16.dp - ListItemDefaults.SegmentedGap
                LazyColumn(
                    state = lazyListState,
                    contentPadding = innerPadding + PaddingValues(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 104.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
                ) {
                    item {
                        Box {
                            val coverModel = playlistEditorUiState.selectedCoverUri
                                ?: playlistEditorUiState.coverFilePath
                            SubcomposeAsyncImage(
                                model = coverModel,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
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

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(16.dp),
                            ) {
                                MuzFilledTonalIconButton(
                                    icon = MuzIcons.Filled.Delete,
                                    onClick = onRemoveCover,
                                    contentDescription = stringResource(localesR.string.core_locales_remove_cover),
                                    enabled = coverModel != null,
                                )
                                MuzFilledTonalIconButton(
                                    icon = MuzIcons.Filled.Edit,
                                    onClick = {
                                        runCatching {
                                            pickMedia.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                                            )
                                        }
                                    },
                                    contentDescription = stringResource(localesR.string.core_locales_set_cover),
                                )
                            }
                        }
                        Spacer(Modifier.size(arrangementPadding))
                    }
                    item {
                        OutlinedTextField(
                            value = playlistEditorUiState.title,
                            onValueChange = onTitleChange,
                            label = { Text(stringResource(localesR.string.core_locales_title)) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                        )
                        Spacer(Modifier.size(arrangementPadding))
                    }
                    item {
                        var shouldShowSongPicker by remember { mutableStateOf(false) }
                        Button(
                            shapes = ButtonDefaults.shapes(),
                            onClick = { shouldShowSongPicker = true },
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = ButtonDefaults.contentPaddingFor(
                                buttonHeight = ButtonDefaults.MinHeight,
                                hasStartIcon = true,
                            ),
                        ) {
                            Icon(
                                imageVector = MuzIcons.Rounded.MusicNote,
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.iconSizeFor(ButtonDefaults.MinHeight)),
                            )
                            Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(ButtonDefaults.MinHeight)))
                            Text(
                                text = stringResource(localesR.string.core_locales_add_songs),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        Spacer(Modifier.size(arrangementPadding))
                        if (shouldShowSongPicker) {
                            SongPickerBottomSheet(
                                onDismiss = { shouldShowSongPicker = false },
                                onSongsSelected = onAddSongs,
                            )
                        }
                    }
                    itemsIndexed(
                        items = playlistEditorUiState.songs,
                        key = { _, song -> song.mediaId },
                        contentType = { _, _ -> "Song" },
                    ) { index, song ->
                        ReorderableItem(
                            state = reorderableLazyListState,
                            key = song.mediaId,
                        ) { _ ->
                            SwipeableItem(
                                modifier = modifier,
                                startToEndSwipeAction = rememberRemoveFromPlaylistSwipeAction(song, onRemoveSong),
                                endToStartSwipeAction = rememberRemoveFromPlaylistSwipeAction(song, onRemoveSong),
                            ) {
                                MuzSelectableListItem(
                                    shapes = if (playlistEditorUiState.songs.size == 1) {
                                        ListItemDefaults.shapes(shape = MaterialTheme.shapes.large)
                                    } else {
                                        ListItemDefaults.segmentedShapes(index, playlistEditorUiState.songs.size)
                                    },
                                    onClick = {},
                                    selected = false,
                                    content = {
                                        Text(
                                            text = song.title,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = song.artist,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    },
                                    leadingContent = {
                                        SubcomposeAsyncImage(
                                            modifier = Modifier
                                                .size(56.dp)
                                                .clip(MaterialTheme.shapes.medium),
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(song.artworkUri)
                                                .size(128)
                                                .build(),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            error = {
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
                                                ) {
                                                    Icon(
                                                        imageVector = MuzIcons.Rounded.MusicNote,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(32.dp),
                                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    )
                                                }
                                            },
                                        )
                                    },
                                    trailingContent = {
                                        Icon(
                                            imageVector = MuzIcons.Rounded.DragHandle,
                                            contentDescription = null,
                                            modifier = Modifier.draggableHandle(),
                                        )
                                    },
                                    modifier = Modifier,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}