package ru.resodostudio.muzyakich.feature.song.detail.impl

import android.app.Activity.RESULT_OK
import android.provider.MediaStore
import android.text.format.Formatter
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import ru.resodostudio.cashsense.core.ui.util.asFormattedBitDepth
import ru.resodostudio.cashsense.core.ui.util.asFormattedSampleRate
import ru.resodostudio.cashsense.core.ui.util.asFormattedString
import ru.resodostudio.muzyakich.core.designsystem.component.AnimatedIcon
import ru.resodostudio.muzyakich.core.designsystem.component.MuzListItem
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSwitch
import ru.resodostudio.muzyakich.core.designsystem.component.MuzTag
import ru.resodostudio.muzyakich.core.designsystem.component.MuzToggableListItem
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.AutoDelete
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.BarChart
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Cadence
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Event
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.HardDrive
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.HighQuality
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.PlayCircle
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.PlaylistPlay
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Schedule
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Star
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Genres
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Star
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun SongBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SongViewModel = hiltViewModel(),
) {
    val songUiState by viewModel.songUiState.collectAsStateWithLifecycle()

    SongBottomSheet(
        songUiState = songUiState,
        onDismiss = onDismiss,
        onSongRemove = viewModel::removeSong,
        modifier = modifier,
        onPlayNextClick = viewModel::playSongNext,
        onFavoriteChange = viewModel::setSongFavorite,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SongBottomSheet(
    songUiState: SongUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onPlayNextClick: (Song) -> Unit = {},
    onFavoriteChange: (String, Boolean) -> Unit = { _, _ -> },
    onSongRemove: (String) -> Unit,
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
                }
                TagPanel(
                    song = song,
                )
                HorizontalDivider()
                ActionPanel(
                    song = song,
                    onPlayNextClick = { song ->
                        onPlayNextClick(song)
                        onDismiss()
                    },
                    onFavoriteChange = onFavoriteChange,
                    onDismiss = onDismiss,
                    onSongRemove = onSongRemove,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ActionPanel(
    song: Song,
    modifier: Modifier = Modifier,
    onPlayNextClick: (Song) -> Unit = {},
    onFavoriteChange: (String, Boolean) -> Unit = { _, _ -> },
    onDismiss: () -> Unit,
    onSongRemove: (String) -> Unit = {},
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
        MuzToggableListItem(
            checked = song.isFavorite,
            onCheckedChange = { checked -> onFavoriteChange(song.mediaId, checked) },
            content = {
                Text(
                    text = stringResource(localesR.string.core_locales_favorites),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            leadingContent = {
                AnimatedIcon(
                    icon = if (song.isFavorite) MuzIcons.Filled.Star else MuzIcons.Rounded.Star,
                    contentDescription = null,
                )
            },
            trailingContent = {
                MuzSwitch(
                    checked = song.isFavorite,
                    onCheckedChange = null,
                )
            },
            colors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
            shapes = ListItemDefaults.segmentedShapes(0, 3),
        )
        MuzListItem(
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
            shapes = ListItemDefaults.segmentedShapes(1, 3),
        )
        MuzListItem(
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
                        listOf(song.mediaUri),
                        true,
                    )
                    launcher.launch(IntentSenderRequest.Builder(pendingIntent.intentSender).build())
                }.onFailure { exception ->
                    exception.printStackTrace()
                }
            },
            shapes = ListItemDefaults.segmentedShapes(2, 3),
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
        if (song.year != 0) {
            MuzTag(
                text = song.year.toString(),
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