package ru.resodostudio.muzyakich.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSelectableListItem
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Delete
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.DragHandle
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.model.data.Song
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QueueItem(
    song: Song,
    shapes: ListItemShapes,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.settledValue) {
        if (dismissState.settledValue == SwipeToDismissBoxValue.StartToEnd || dismissState.settledValue == SwipeToDismissBoxValue.EndToStart) {
            onDismiss()
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = {
            val direction = dismissState.dismissDirection

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .layout { measurable, constraints ->
                            val offset = try {
                                dismissState.requireOffset().let { if (it.isNaN()) 0f else it }.absoluteValue.roundToInt()
                            } catch (e: Exception) {
                                0
                            }

                            val width = offset.coerceIn(0, constraints.maxWidth)
                            val placeable = measurable.measure(
                                constraints.copy(minWidth = width, maxWidth = width)
                            )
                            layout(constraints.maxWidth, constraints.maxHeight) {
                                if (direction == SwipeToDismissBoxValue.EndToStart) {
                                    placeable.placeRelative(constraints.maxWidth - width, 0)
                                } else {
                                    placeable.placeRelative(0, 0)
                                }
                            }
                        }
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.errorContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = MuzIcons.Filled.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                    )
                }
            }
        },
        content = {
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
                            .clip(MaterialTheme.shapes.small),
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
    )
}
