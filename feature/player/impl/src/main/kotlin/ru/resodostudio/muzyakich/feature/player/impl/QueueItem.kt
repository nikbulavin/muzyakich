package ru.resodostudio.muzyakich.feature.player.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import ru.resodostudio.cashsense.core.ui.SwipeableItem
import ru.resodostudio.cashsense.core.ui.rememberRemoveFromQueueSwipeAction
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSelectableListItem
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.DragHandle
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.model.QueueSong

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun QueueItem(
    song: QueueSong,
    shapes: ListItemShapes,
    modifier: Modifier = Modifier,
    reorderableModifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onRemoveFromQueue: (String) -> Unit = {},
) {
    SwipeableItem(
        modifier = modifier,
        startToEndSwipeAction = rememberRemoveFromQueueSwipeAction(song, onRemoveFromQueue),
        endToStartSwipeAction = rememberRemoveFromQueueSwipeAction(song, onRemoveFromQueue),
    ) {
        MuzSelectableListItem(
            shapes = shapes,
            selected = false,
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
                IconButton(
                    onClick = {},
                    shapes = IconButtonDefaults.shapes(),
                    modifier = reorderableModifier,
                ) {
                    Icon(
                        imageVector = MuzIcons.Rounded.DragHandle,
                        contentDescription = null,
                    )
                }
            },
            colors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
        )
    }
}
