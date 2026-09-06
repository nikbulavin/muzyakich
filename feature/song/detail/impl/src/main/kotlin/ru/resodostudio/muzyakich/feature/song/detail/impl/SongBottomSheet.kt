package ru.resodostudio.muzyakich.feature.song.detail.impl

import android.app.Activity.RESULT_OK
import android.provider.MediaStore
import android.text.format.Formatter
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconToggleButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSegmentedListItem
import ru.resodostudio.muzyakich.core.designsystem.component.MuzTag
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.AutoDelete
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.BarChart
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Cadence
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Edit
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Event
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.HardDrive
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.HighQuality
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.PlayCircle
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.PlaylistPlay
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Schedule
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Star
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Genres
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlaylistAdd
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Star
import ru.resodostudio.muzyakich.core.model.Playlist
import ru.resodostudio.muzyakich.core.model.Song
import ru.resodostudio.muzyakich.core.ui.util.asFormattedBitDepth
import ru.resodostudio.muzyakich.core.ui.util.asFormattedSampleRate
import ru.resodostudio.muzyakich.core.ui.util.asFormattedString
import ru.resodostudio.muzyakich.feature.song.detail.impl.component.PlaylistPicker
import kotlin.uuid.Uuid
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun SongBottomSheet(
    onDismiss: () -> Unit,
    onEditTagsClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SongViewModel = hiltViewModel(),
) {
    val songUiState by viewModel.songUiState.collectAsStateWithLifecycle()
    val activity = LocalActivity.current

    SongBottomSheet(
        songUiState = songUiState,
        onDismiss = onDismiss,
        onEditTagsClick = onEditTagsClick,
        onSongRemove = viewModel::removeSong,
        modifier = modifier,
        onPlayNextClick = viewModel::playSongNext,
        onFavoriteChange = { id, favorite ->
            activity?.let { viewModel.setSongFavorite(id, favorite, it) }
        },
        onAddSongToPlaylist = viewModel::addToPlaylist,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SongBottomSheet(
    songUiState: SongUiState,
    onDismiss: () -> Unit,
    onEditTagsClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onPlayNextClick: (Song) -> Unit = {},
    onFavoriteChange: (String, Boolean) -> Unit = { _, _ -> },
    onSongRemove: (String) -> Unit,
    onAddSongToPlaylist: (Uuid, String, Int) -> Unit = { _, _, _ -> },
) {
    when (songUiState) {
        SongUiState.Error -> onDismiss()
        SongUiState.Loading -> LoadingIndicator(Modifier.fillMaxWidth())
        is SongUiState.Success -> {
            val song = songUiState.song
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .size(82.dp)
                            .clip(MaterialTheme.shapes.medium),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(song.artworkUri)
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
                                    modifier = Modifier.size(50.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        },
                    )
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = song.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = song.artist,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = song.album,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    val (icon, contentDescription) = if (song.isFavorite) {
                        MuzIcons.Filled.Star to stringResource(localesR.string.core_locales_remove_from_favorites)
                    } else {
                        MuzIcons.Rounded.Star to stringResource(localesR.string.core_locales_add_to_favorites)
                    }
                    MuzIconToggleButton(
                        checked = song.isFavorite,
                        onCheckedChange = { onFavoriteChange(song.mediaId, it) },
                        icon = icon,
                        contentDescription = contentDescription,
                    )
                }
                TagPanel(
                    song = song,
                )
                HorizontalDivider()
                ActionPanel(
                    song = song,
                    availablePlaylists = songUiState.playlists,
                    onPlayNextClick = { song ->
                        onPlayNextClick(song)
                        onDismiss()
                    },
                    onEditTagsClick = { mediaId ->
                        onEditTagsClick(mediaId)
                    },
                    onDismiss = onDismiss,
                    onSongRemove = onSongRemove,
                    onAddSongToPlaylist = onAddSongToPlaylist,
                )
            }
        }
    }
}

@Composable
private fun ActionPanel(
    song: Song,
    availablePlaylists: List<Playlist>,
    modifier: Modifier = Modifier,
    onPlayNextClick: (Song) -> Unit = {},
    onEditTagsClick: (String) -> Unit = {},
    onDismiss: () -> Unit,
    onSongRemove: (String) -> Unit = {},
    onAddSongToPlaylist: (Uuid, String, Int) -> Unit = { _, _, _ -> },
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                onSongRemove(song.mediaId)
                onDismiss()
            }
        }
        var shouldShowPlaylistPicker by rememberSaveable { mutableStateOf(false) }
        MuzSegmentedListItem(
            enabled = availablePlaylists.isNotEmpty(),
            content = {
                Text(
                    text = stringResource(localesR.string.core_locales_add_to_playlist),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            leadingContent = {
                Icon(
                    imageVector = MuzIcons.Rounded.PlaylistAdd,
                    contentDescription = null,
                )
            },
            colors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
            onClick = { shouldShowPlaylistPicker = true },
            shapes = ListItemDefaults.segmentedShapes(0, 4),
        )
        if (shouldShowPlaylistPicker) {
            PlaylistPicker(
                songMediaId = song.mediaId,
                availablePlaylists = availablePlaylists,
                onConfirm = onAddSongToPlaylist,
                onDismiss = { shouldShowPlaylistPicker = false },
            )
        }
        MuzSegmentedListItem(
            content = {
                Text(
                    text = stringResource(localesR.string.core_locales_play_next),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            leadingContent = {
                Icon(
                    imageVector = MuzIcons.Filled.PlaylistPlay,
                    contentDescription = null,
                )
            },
            colors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
            onClick = { onPlayNextClick(song) },
            shapes = ListItemDefaults.segmentedShapes(1, 4),
        )
        MuzSegmentedListItem(
            content = {
                Text(
                    text = stringResource(localesR.string.core_locales_edit_tags),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            leadingContent = {
                Icon(
                    imageVector = MuzIcons.Filled.Edit,
                    contentDescription = null,
                )
            },
            colors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
            onClick = { onEditTagsClick(song.mediaId) },
            shapes = ListItemDefaults.segmentedShapes(2, 4),
        )
        MuzSegmentedListItem(
            content = {
                Text(
                    text = stringResource(localesR.string.core_locales_move_to_trash),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            leadingContent = {
                Icon(
                    imageVector = MuzIcons.Filled.AutoDelete,
                    contentDescription = null,
                )
            },
            supportingContent = {
                Text(
                    text = stringResource(localesR.string.core_locales_move_to_trash_description),
                    maxLines = 1,
                    overflow = TextOverflow.StartEllipsis,
                )
            },
            colors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
            onClick = {
                runCatching {
                    val pendingIntent = MediaStore.createTrashRequest(
                        context.contentResolver,
                        listOf(song.mediaUri.toUri()),
                        true,
                    )
                    launcher.launch(IntentSenderRequest.Builder(pendingIntent.intentSender).build())
                }.onFailure { exception ->
                    exception.printStackTrace()
                }
            },
            shapes = ListItemDefaults.segmentedShapes(3, 4),
        )
    }
}

@Composable
private fun TagPanel(
    song: Song,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        MuzTag(
            text = (song.duration / 1000).asFormattedString(),
            icon = MuzIcons.Filled.Schedule,
        )
        song.year?.let { year ->
            MuzTag(
                text = year.toString(),
                icon = MuzIcons.Filled.Event,
            )
        }
        song.genre?.let { genre ->
            MuzTag(
                text = genre,
                icon = MuzIcons.Rounded.Genres,
            )
        }
        if (song.playCount > 0) {
            MuzTag(
                text = pluralStringResource(
                    id = localesR.plurals.core_locales_plays_count,
                    count = song.playCount,
                    song.playCount,
                ),
                icon = MuzIcons.Filled.PlayCircle,
            )
        }
        MuzTag(
            text = stringResource(localesR.string.core_locales_bitrate_format, song.bitrate),
            icon = if (song.bitrate >= 256) MuzIcons.Filled.HighQuality else MuzIcons.Filled.BarChart,
        )
        AudioQualityTag(
            song = song,
        )
        MuzTag(
            text = Formatter.formatFileSize(LocalContext.current, song.size.toLong()),
            icon = MuzIcons.Filled.HardDrive,
        )
    }
}

@Composable
private fun AudioQualityTag(
    song: Song,
    modifier: Modifier = Modifier,
) {
    val resolutionLabels = listOfNotNull(
        song.bitsPerSample?.asFormattedBitDepth(),
        (song.sampleRate?.div(1000f))?.asFormattedSampleRate(),
    )
    if (resolutionLabels.isNotEmpty()) {
        MuzTag(
            text = resolutionLabels.joinToString(" "),
            icon = MuzIcons.Filled.Cadence,
            modifier = modifier,
        )
    }
}
