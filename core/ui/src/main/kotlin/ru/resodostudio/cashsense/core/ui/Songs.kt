package ru.resodostudio.cashsense.core.ui

import android.text.format.Formatter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.dropUnlessResumed
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import kotlinx.coroutines.launch
import ru.resodostudio.cashsense.core.ui.util.asFormattedDuration
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSelectableListItem
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MoreVert
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.ui.R
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import ru.resodostudio.muzyakich.core.locales.R as localesR

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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SongItem(
    song: Song,
    shapes: ListItemShapes,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    onClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    startToEndSwipeAction: SwipeAction? = null,
    endToStartSwipeAction: SwipeAction? = null,
) {
    SwipeableItem(
        modifier = modifier,
        startToEndSwipeAction = startToEndSwipeAction,
        endToStartSwipeAction = endToStartSwipeAction,
    ) {
        MuzSelectableListItem(
            shapes = shapes,
            selected = isPlaying,
            onClick = onClick,
            onLongClick = dropUnlessResumed { onMenuClick() },
            onLongClickLabel = stringResource(localesR.string.core_locales_open_menu),
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
                Box {
                    val shape = MaterialTheme.shapes.medium
                    if (isPlaying) {
                        Box(
                            modifier = Modifier
                                .zIndex(1f)
                                .size(56.dp)
                                .clip(shape)
                                .background(MaterialTheme.colorScheme.surface.copy(0.6f)),
                        ) {
                            val dynamicProperties = rememberLottieDynamicProperties(
                                rememberLottieDynamicProperty(
                                    property = LottieProperty.COLOR,
                                    value = MaterialTheme.colorScheme.secondary.toArgb(),
                                    keyPath = arrayOf("**"),
                                ),
                            )
                            val lottieComposition by rememberLottieComposition(
                                LottieCompositionSpec.RawRes(R.raw.core_ui_equalizer_anim),
                            )
                            val progress by animateLottieCompositionAsState(
                                composition = lottieComposition,
                                iterations = LottieConstants.IterateForever,
                                speed = 0.4f,
                            )
                            LottieAnimation(
                                modifier = Modifier
                                    .zIndex(2f)
                                    .align(Alignment.Center)
                                    .size(40.dp),
                                composition = lottieComposition,
                                progress = { progress },
                                dynamicProperties = dynamicProperties,
                            )
                        }
                    }
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(shape),
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
                }
            },
            trailingContent = {
                MuzIconButton(
                    onClick = onMenuClick,
                    icon = MuzIcons.Rounded.MoreVert,
                    contentDescription = stringResource(localesR.string.core_locales_open_menu),
                    modifier = Modifier.size(
                        IconButtonDefaults.smallContainerSize(
                            IconButtonDefaults.IconButtonWidthOption.Narrow,
                        ),
                    ),
                )
            },
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun LazyGridScope.songs(
    songs: List<Song>,
    currentMediaId: String?,
    onPlaySongsClick: (List<Song>, Int) -> Unit,
    onSongMenuClick: (String) -> Unit,
    isPlaying: Boolean = false,
    modifier: Modifier = Modifier,
    startToEndSwipeAction: @Composable ((Song) -> SwipeAction?)? = null,
    endToStartSwipeAction: @Composable ((Song) -> SwipeAction?)? = null,
) {
    itemsIndexed(
        items = songs,
        key = { _, song -> song.mediaId },
        contentType = { _, _ -> "Song" },
    ) { index, song ->
        SongItem(
            song = song,
            isPlaying = currentMediaId == song.mediaId && isPlaying,
            modifier = modifier.animateItem(),
            onClick = { onPlaySongsClick(songs, songs.indexOf(song)) },
            onMenuClick = { onSongMenuClick(song.mediaId) },
            shapes = if (songs.size == 1) {
                ListItemDefaults.shapes(shape = MaterialTheme.shapes.large)
            } else {
                ListItemDefaults.segmentedShapes(index, songs.size)
            },
            startToEndSwipeAction = startToEndSwipeAction?.invoke(song),
            endToStartSwipeAction = endToStartSwipeAction?.invoke(song),
        )
    }
}

fun LazyGridScope.songsInfo(
    songs: List<Song>,
    modifier: Modifier = Modifier,
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
    ) {
        val count = pluralStringResource(localesR.plurals.core_locales_number_of_songs, songs.size, songs.size)
        val overallDuration = songs
            .sumOf { it.duration }
            .asFormattedDuration()
        val sizeOnDisk = Formatter.formatFileSize(
            LocalContext.current,
            songs.sumOf { it.size }.toLong(),
        )
        val songsInfo = listOf(count, overallDuration, sizeOnDisk)
        Text(
            text = songsInfo.joinToString(),
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .animateItem(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}