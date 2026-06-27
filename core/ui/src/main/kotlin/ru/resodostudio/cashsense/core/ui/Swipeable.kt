package ru.resodostudio.cashsense.core.ui

import android.app.Activity.RESULT_OK
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Delete
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.PlaylistPlay
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Remove
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlaylistRemove
import ru.resodostudio.muzyakich.core.model.QueueSong
import ru.resodostudio.muzyakich.core.model.Song
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SwipeableItem(
    modifier: Modifier = Modifier,
    startToEndSwipeAction: SwipeAction? = null,
    endToStartSwipeAction: SwipeAction? = null,
    content: @Composable () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState()
    val scope = rememberCoroutineScope()

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = startToEndSwipeAction != null,
        enableDismissFromEndToStart = endToStartSwipeAction != null,
        onDismiss = { dismissDirection ->
            if (dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                startToEndSwipeAction?.action?.invoke()
            } else if (dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                endToStartSwipeAction?.action?.invoke()
            }
            scope.launch {
                dismissState.reset()
            }
        },
        backgroundContent = {
            val isStartToEnd = dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd
            val swipeAction = if (isStartToEnd) startToEndSwipeAction else endToStartSwipeAction

            if (swipeAction != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .layout { measurable, constraints ->
                                val offset = runCatching {
                                    dismissState.requireOffset()
                                        .let { if (it.isNaN()) 0f else it }.absoluteValue.roundToInt()
                                }.getOrDefault(0)

                                val gap = 2.dp.roundToPx()
                                val width = (offset - gap).coerceIn(0, constraints.maxWidth)
                                val placeable = measurable.measure(
                                    constraints.copy(minWidth = width, maxWidth = width),
                                )
                                layout(constraints.maxWidth, constraints.maxHeight) {
                                    if (isStartToEnd) {
                                        placeable.placeRelative(0, 0)
                                    } else {
                                        placeable.placeRelative(constraints.maxWidth - width, 0)
                                    }
                                }
                            }
                            .clip(MaterialTheme.shapes.extraLargeIncreased)
                            .background(swipeAction.backgroundColor),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = swipeAction.icon,
                            contentDescription = null,
                            tint = swipeAction.iconColor,
                        )
                    }
                }
            }
        },
        content = {
            content()
        },
    )
}

@Composable
fun rememberDeleteSwipeAction(
    song: Song,
    onSongRemove: (String) -> Unit,
): SwipeAction {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
    ) { result ->
        if (result.resultCode == RESULT_OK) onSongRemove(song.mediaId)
    }
    return SwipeAction(
        icon = MuzIcons.Filled.Delete,
        backgroundColor = MaterialTheme.colorScheme.errorContainer,
        iconColor = MaterialTheme.colorScheme.onErrorContainer,
        action = {
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
    )
}

@Composable
fun rememberRemoveFromPlaylistSwipeAction(
    song: Song,
    onRemove: (Song) -> Unit,
): SwipeAction {
    return SwipeAction(
        icon = MuzIcons.Rounded.PlaylistRemove,
        backgroundColor = MaterialTheme.colorScheme.errorContainer,
        iconColor = MaterialTheme.colorScheme.onErrorContainer,
        action = { onRemove(song) },
    )
}

@Composable
fun rememberPlaylistPlaySwipeAction(
    song: Song,
    onSwipe: (Song) -> Unit,
): SwipeAction {
    return SwipeAction(
        icon = MuzIcons.Filled.PlaylistPlay,
        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
        iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
        action = { onSwipe(song) },
    )
}

@Composable
fun rememberRemoveFromQueueSwipeAction(
    queueSong: QueueSong,
    onRemove: (String) -> Unit,
): SwipeAction {
    return SwipeAction(
        icon = MuzIcons.Filled.Remove,
        backgroundColor = MaterialTheme.colorScheme.errorContainer,
        iconColor = MaterialTheme.colorScheme.onErrorContainer,
        action = { onRemove(queueSong.uid) },
    )
}


data class SwipeAction(
    val icon: ImageVector,
    val backgroundColor: Color,
    val iconColor: Color,
    val action: () -> Unit,
)