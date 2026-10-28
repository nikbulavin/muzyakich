package ru.resodostudio.muzyakich.core.ui

import android.text.format.Formatter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSelectableListItem
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MoreVert
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.model.Song
import ru.resodostudio.muzyakich.core.ui.util.asFormattedDuration
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongItem(
    song: Song,
    shapes: ListItemShapes,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    shouldShowArtwork: Boolean = true,
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
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center,
                ) {
                    if (shouldShowArtwork) {
                        SubcomposeAsyncImage(
                            modifier = Modifier.matchParentSize(),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(song.artworkUri)
                                .size(128)
                                .memoryCacheKey("${song.mediaUri}_${song.dateModified}")
                                .diskCacheKey("${song.mediaUri}_${song.dateModified}")
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
                    } else if (!isPlaying) {
                        Text(
                            text = (song.trackNumber % 1000).toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                        )
                    }

                    if (isPlaying) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .then(
                                    if (shouldShowArtwork) {
                                        Modifier.background(MaterialTheme.colorScheme.surface.copy(0.6f))
                                    } else {
                                        Modifier
                                    },
                                ),
                            contentAlignment = Alignment.Center,
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
                                modifier = Modifier.size(40.dp),
                                composition = lottieComposition,
                                progress = { progress },
                                dynamicProperties = dynamicProperties,
                            )
                        }
                    }
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

fun LazyGridScope.songs(
    songs: List<Song>,
    currentMediaId: String?,
    onPlaySongsClick: (List<Song>, Int) -> Unit,
    onSongMenuClick: (String) -> Unit,
    isPlaying: Boolean = false,
    shouldShowArtworks: Boolean = true,
    modifier: Modifier = Modifier,
    startToEndSwipeAction: @Composable ((Song) -> SwipeAction?)? = null,
    endToStartSwipeAction: @Composable ((Song) -> SwipeAction?)? = null,
) {
    val showArtwork = shouldShowArtworks || !songs.all { it.trackNumber > 0 }

    itemsIndexed(
        items = songs,
        key = { _, song -> song.mediaId },
        contentType = { _, _ -> "Song" },
    ) { index, song ->
        SongItem(
            song = song,
            isPlaying = currentMediaId == song.mediaId && isPlaying,
            shouldShowArtwork = showArtwork,
            modifier = modifier.animateItem(),
            onClick = { onPlaySongsClick(songs, index) },
            onMenuClick = { onSongMenuClick(song.mediaId) },
            shapes = ListItemDefaults.segmentedShapes(index, songs.size),
            startToEndSwipeAction = startToEndSwipeAction?.invoke(song),
            endToStartSwipeAction = endToStartSwipeAction?.invoke(song),
        )
    }
}

fun LazyGridScope.songsInfo(
    songs: List<Song>,
    modifier: Modifier = Modifier,
) {
    if (songs.isEmpty()) return

    item(
        span = { GridItemSpan(maxLineSpan) },
    ) {
        val (totalDurationMs, totalSizeBytes) = remember(songs) {
            var duration = 0L
            var size = 0L
            songs.forEach { song ->
                duration += song.duration
                size += song.size
            }
            duration to size
        }

        val count = pluralStringResource(
            localesR.plurals.core_locales_number_of_songs,
            songs.size,
            songs.size,
        )
        val overallDuration = totalDurationMs.asFormattedDuration()
        val sizeOnDisk = Formatter.formatShortFileSize(LocalContext.current, totalSizeBytes)

        val infoParts = listOf(count, overallDuration, sizeOnDisk)
        Text(
            text = infoParts.joinToString(),
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
