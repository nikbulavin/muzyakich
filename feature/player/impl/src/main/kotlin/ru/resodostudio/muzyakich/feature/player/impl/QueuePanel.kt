package ru.resodostudio.muzyakich.feature.player.impl

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.resodostudio.cashsense.core.ui.SongArtworkMini
import ru.resodostudio.muzyakich.core.designsystem.theme.sharedElementTransitionSpec
import ru.resodostudio.muzyakich.core.model.data.QueueSong
import ru.resodostudio.muzyakich.core.model.data.Song
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun QueuePanel(
    currentSong: Song,
    playingQueue: List<QueueSong>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    onQueueItemClick: (String) -> Unit = {},
    onFavoriteChange: (String, Boolean) -> Unit = { _, _ -> },
    onSongLongClick: (String) -> Unit = {},
    onRemoveFromQueue: (String) -> Unit = {},
    onReorderSongs: (String, String) -> Unit = { _, _ -> },
) {
    with(sharedTransitionScope) {
        Column(
            modifier = modifier,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SongArtworkMini(
                    artworkUri = currentSong.artworkUri,
                    modifier = Modifier
                        .sharedBounds(
                            boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                            sharedContentState = rememberSharedContentState(currentSong.artworkUri.toString()),
                            animatedVisibilityScope = animatedVisibilityScope,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                        )
                        .clip(MaterialTheme.shapes.small)
                        .size(64.dp),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = currentSong.title,
                        maxLines = 1,
                        modifier = Modifier
                            .sharedBounds(
                                boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                sharedContentState = rememberSharedContentState(currentSong.title),
                                animatedVisibilityScope = animatedVisibilityScope,
                            )
                            .basicMarquee(),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = currentSong.artist,
                        maxLines = 1,
                        modifier = Modifier
                            .sharedBounds(
                                boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                sharedContentState = rememberSharedContentState(currentSong.artist),
                                animatedVisibilityScope = animatedVisibilityScope,
                            )
                            .basicMarquee(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                FavoriteToggleButton(
                    song = currentSong,
                    onFavoriteChange = onFavoriteChange,
                    modifier = Modifier
                        .sharedBounds(
                            boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                            sharedContentState = rememberSharedContentState(localesR.string.core_locales_favorites),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                )
                MoreIconButton(
                    onClick = { onSongLongClick(currentSong.mediaId) },
                    modifier = Modifier
                        .sharedBounds(
                            boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                            sharedContentState = rememberSharedContentState(localesR.string.core_locales_more_options),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                )
            }
            Text(
                text = stringResource(localesR.string.core_locales_next_in_queue),
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            var localPlayingQueue by remember(playingQueue) { mutableStateOf(playingQueue) }
            var draggedItemId by remember { mutableStateOf<String?>(null) }
            var targetItemId by remember { mutableStateOf<String?>(null) }

            val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
                val fromKey = from.key.toString()
                val toKey = to.key.toString()

                if (draggedItemId == null) draggedItemId = fromKey
                targetItemId = toKey

                val fromIndex = localPlayingQueue.indexOfFirst { it.uid == fromKey }
                val toIndex = localPlayingQueue.indexOfFirst { it.uid == toKey }

                if (fromIndex != -1 && toIndex != -1) {
                    localPlayingQueue = localPlayingQueue.toMutableList().apply {
                        add(toIndex, removeAt(fromIndex))
                    }
                }
            }

            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 14.dp,
                    end = 14.dp,
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
                ),
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
            ) {
                itemsIndexed(
                    items = localPlayingQueue,
                    key = { _, song -> song.uid },
                    contentType = { _, _ -> "QueueSong" },
                ) { index, song ->
                    ReorderableItem(
                        state = reorderableLazyListState,
                        key = song.uid,
                    ) { _ ->
                        QueueItem(
                            song = song,
                            modifier = Modifier,
                            reorderableModifier = Modifier.draggableHandle(
                                onDragStopped = {
                                    val from = draggedItemId
                                    val to = targetItemId
                                    if (from != null && to != null && from != to) {
                                        onReorderSongs(from, to)
                                    }
                                    draggedItemId = null
                                    targetItemId = null
                                },
                            ),
                            onClick = { onQueueItemClick(song.uid) },
                            onDismiss = { onRemoveFromQueue(song.uid) },
                            shapes = if (localPlayingQueue.size == 1) {
                                ListItemDefaults.shapes(shape = MaterialTheme.shapes.large)
                            } else {
                                ListItemDefaults.segmentedShapes(index, localPlayingQueue.size)
                            },
                        )
                    }
                }
            }
        }
    }
}
