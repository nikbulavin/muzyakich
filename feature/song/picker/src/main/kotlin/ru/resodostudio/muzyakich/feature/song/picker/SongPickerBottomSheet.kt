package ru.resodostudio.muzyakich.feature.song.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.FloatingToolbarDefaults.floatingToolbarVerticalNestedScroll
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import ru.resodostudio.cashsense.core.ui.LoadingState
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSelectableListItem
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Check
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Close
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun SongPickerBottomSheet(
    onDismiss: () -> Unit,
    onSongsSelected: (List<Song>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SongPickerViewModel = hiltViewModel(),
) {
    val songPickerUiState by viewModel.songPickerUiState.collectAsStateWithLifecycle()

    SongPickerBottomSheet(
        songPickerUiState = songPickerUiState,
        onDismiss = onDismiss,
        onToggleSong = viewModel::toggleSong,
        onClearSelectedSongs = viewModel::clearSelectedSongs,
        onSongsSelected = { songs ->
            onSongsSelected(songs)
            viewModel.clearSelectedSongs()
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SongPickerBottomSheet(
    songPickerUiState: SongPickerUiState,
    onDismiss: () -> Unit,
    onToggleSong: (String) -> Unit,
    onClearSelectedSongs: () -> Unit,
    onSongsSelected: (List<Song>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        when (songPickerUiState) {
            SongPickerUiState.Error -> onDismiss()
            SongPickerUiState.Loading -> {
                LoadingState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                )
            }

            is SongPickerUiState.Success -> {
                var expanded by rememberSaveable { mutableStateOf(true) }
                Box {
                    HorizontalFloatingToolbar(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = -ScreenOffset)
                            .zIndex(1f),
                        expanded = expanded,
                        content = {
                            val okText = stringResource(localesR.string.core_locales_confirm)
                            TooltipBox(
                                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                                    TooltipAnchorPosition.Above
                                ),
                                tooltip = { PlainTooltip { Text(okText) } },
                                state = rememberTooltipState(),
                                modifier = Modifier.padding(
                                    end = if (expanded) 4.dp else 0.dp,
                                ),
                            ) {
                                FilledIconButton(
                                    modifier = Modifier.width(64.dp),
                                    onClick = {
                                        val selectedSongs = songPickerUiState.songs
                                            .filter { it.mediaId in songPickerUiState.selectedSongs }
                                        onSongsSelected(selectedSongs)
                                        onDismiss()
                                    },
                                    enabled = songPickerUiState.selectedSongs.isNotEmpty(),
                                ) {
                                    Icon(
                                        imageVector = MuzIcons.Rounded.Check,
                                        contentDescription = okText,
                                    )
                                }
                            }
                        },
                        leadingContent = {
                            val clearText = stringResource(localesR.string.core_locales_clear)
                            AppBarRow(
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                clickableItem(
                                    onClick = onClearSelectedSongs,
                                    icon = {
                                        Icon(
                                            imageVector = MuzIcons.Rounded.Close,
                                            contentDescription = clearText,
                                        )
                                    },
                                    label = clearText,
                                    enabled = songPickerUiState.selectedSongs.isNotEmpty()
                                )
                            }
                        },
                    )
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.floatingToolbarVerticalNestedScroll(
                            expanded = expanded,
                            onExpand = { expanded = true },
                            onCollapse = { expanded = false },
                        ),
                        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
                    ) {
                        songs(
                            songs = songPickerUiState.songs,
                            selectedSongs = songPickerUiState.selectedSongs,
                            onClick = onToggleSong,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun LazyListScope.songs(
    songs: List<Song>,
    selectedSongs: Set<String>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    itemsIndexed(
        items = songs,
        key = { _, song -> song.mediaId },
        contentType = { _, _ -> "Song" },
    ) { index, song ->
        val selected = selectedSongs.contains(song.mediaId)
        SongItem(
            song = song,
            selected = selected,
            modifier = modifier.animateItem(),
            onClick = { onClick(song.mediaId) },
            shapes = if (songs.size == 1) {
                ListItemDefaults.shapes(shape = MaterialTheme.shapes.large)
            } else {
                ListItemDefaults.segmentedShapes(index, songs.size)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SongItem(
    song: Song,
    shapes: ListItemShapes,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    MuzSelectableListItem(
        colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
        modifier = modifier,
        shapes = shapes,
        selected = selected,
        onClick = onClick,
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
            Checkbox(
                checked = selected,
                onCheckedChange = null,
            )
        },
    )
}
