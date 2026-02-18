package ru.resodostudio.muzyakich.ui.component

import android.os.Build
import android.os.ext.SdkExtensions
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ru.resodostudio.muzyakich.core.designsystem.component.AnimatedIcon
import ru.resodostudio.muzyakich.core.designsystem.component.MuzListItem
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSwitch
import ru.resodostudio.muzyakich.core.designsystem.component.MuzTag
import ru.resodostudio.muzyakich.core.designsystem.component.MuzToggableListItem
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.BarChart
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Cadence
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Delete
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.HardDrive
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.HighQuality
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.PlaylistPlay
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Schedule
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Star
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Star
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.ui.util.asFormattedBitDepth
import ru.resodostudio.muzyakich.ui.util.asFormattedSampleRate
import ru.resodostudio.muzyakich.ui.util.asFormattedString
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongDetailsBottomSheet(
    song: Song,
    onDismiss: () -> Unit,
    onPlayNextClick: (Song) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .size(82.dp)
                        .clip(MaterialTheme.shapes.medium),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(song.artworkUri)
                        .crossfade(true)
                        .size(256)
                        .build(),
                    contentDescription = null,
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
                modifier = Modifier.padding(16.dp),
            )
            HorizontalDivider()
            ActionPanel(
                song = song,
                modifier = Modifier.padding(16.dp),
                onPlayNextClick = { song ->
                    onPlayNextClick(song)
                    onDismiss()
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ActionPanel(
    song: Song,
    modifier: Modifier = Modifier,
    onPlayNextClick: (Song) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
        ) {}
        MuzToggableListItem(
            checked = song.isFavorite,
            onCheckedChange = { checked ->
                runCatching {
                    val pendingIntent = MediaStore.createFavoriteRequest(
                        context.contentResolver,
                        listOf(song.mediaUri),
                        checked,
                    )
                    launcher.launch(IntentSenderRequest.Builder(pendingIntent.intentSender).build())
                }
            },
            content = {
                Text(
                    text = stringResource(localesR.string.favorites),
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
                    text = stringResource(localesR.string.play_next),
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
                    text = stringResource(localesR.string.move_to_trash),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            leadingContent = {
                Icon(
                    imageVector = MuzIcons.Filled.Delete,
                    contentDescription = null,
                )
            },
            supportingContent = {
                Text(
                    text = stringResource(localesR.string.move_to_trash_description),
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
        MuzTag(
            text = stringResource(localesR.string.bitrate_format, song.bitrate),
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
    if (SdkExtensions.getExtensionVersion(Build.VERSION_CODES.TIRAMISU) >= 15) {
        val audioQualityText = buildString {
            if (song.bitsPerSample > 0) {
                append(song.bitsPerSample.asFormattedBitDepth())
            }
            if (song.bitsPerSample > 0 && song.sampleRate > 0) {
                append(" ")
            }
            if (song.sampleRate > 0) {
                append((song.sampleRate / 1000f).asFormattedSampleRate())
            }
        }
        MuzTag(
            text = audioQualityText,
            icon = MuzIcons.Filled.Cadence,
            modifier = modifier,
        )
    }
}