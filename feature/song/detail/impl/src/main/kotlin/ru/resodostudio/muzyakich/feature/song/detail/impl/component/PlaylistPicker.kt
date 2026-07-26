package ru.resodostudio.muzyakich.feature.song.detail.impl.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSelectableListItem
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlaylistAdd
import ru.resodostudio.muzyakich.core.model.Playlist
import kotlin.uuid.Uuid
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun PlaylistPicker(
    songMediaId: String,
    availablePlaylists: List<Playlist>,
    onConfirm: (Uuid, String, Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedPlaylistState by remember { mutableStateOf(availablePlaylists.firstOrNull()) }

    AlertDialog(
        title = {
            Text(
                text = stringResource(localesR.string.core_locales_add_to_playlist),
                textAlign = TextAlign.Center,
            )
        },
        icon = {
            Icon(
                imageVector = MuzIcons.Rounded.PlaylistAdd,
                contentDescription = null,
            )
        },
        onDismissRequest = onDismiss,
        modifier = modifier,
        confirmButton = {
            Button(
                onClick = {
                    selectedPlaylistState?.let { selectedPlaylist ->
                        onConfirm(
                            selectedPlaylist.uuid,
                            songMediaId,
                            selectedPlaylistState!!.songs.size + 1,
                        )
                    }
                    onDismiss()
                },
                shapes = ButtonDefaults.shapes(),
            ) {
                Text(stringResource(localesR.string.core_locales_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shapes = ButtonDefaults.shapes(),
            ) {
                Text(stringResource(localesR.string.core_locales_cancel))
            }
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
            ) {
                itemsIndexed(
                    items = availablePlaylists,
                    key = { _, playlist -> playlist.uuid },
                ) { index, playlist ->
                    val selected = playlist == selectedPlaylistState
                    MuzSelectableListItem(
                        leadingContent = {
                            SubcomposeAsyncImage(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(MaterialTheme.shapes.medium),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(playlist.coverFilePath)
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
                        supportingContent = {
                            Text(
                                text = pluralStringResource(
                                    localesR.plurals.core_locales_number_of_songs,
                                    playlist.songs.size,
                                    playlist.songs.size,
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        content = {
                            Text(
                                text = playlist.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        selected = selected,
                        onClick = { selectedPlaylistState = playlist },
                        trailingContent = {
                            RadioButton(
                                selected = selected,
                                onClick = null,
                            )
                        },
                        shapes = ListItemDefaults.segmentedShapes(index, availablePlaylists.size),
                        colors = ListItemDefaults.segmentedColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        ),
                    )
                }
            }
        },
    )
}