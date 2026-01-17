package ru.resodostudio.muzyakich.ui.component

import android.provider.MediaStore
import android.text.format.Formatter
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import ru.resodostudio.muzyakich.R
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconToggleButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSelectableListItem
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Star
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Star
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.ui.util.asFormattedDuration
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SongItem(
    song: Song,
    shapes: ListItemShapes,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    onClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
) {
    MuzSelectableListItem(
        shapes = shapes,
        selected = isPlaying,
        onClick = onClick,
        onLongClick = onMenuClick,
        onLongClickLabel = stringResource(localesR.string.open_menu),
        modifier = modifier,
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
                if (isPlaying) {
                    Box(
                        modifier = Modifier
                            .zIndex(1f)
                            .size(56.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.surface.copy(0.6f)),
                    ) {
                        val dynamicProperties = rememberLottieDynamicProperties(
                            rememberLottieDynamicProperty(
                                property = LottieProperty.COLOR,
                                value = MaterialTheme.colorScheme.secondary.toArgb(),
                                keyPath = arrayOf("**"),
                            )
                        )
                        val lottieComposition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.equalizer_anim)
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
                        .clip(MaterialTheme.shapes.small),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(song.artworkUri)
                        .crossfade(true)
                        .size(128)
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
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    },
                )
            }
        },
        trailingContent = {
            val (icon, contentDescription) = if (song.isFavorite) {
                MuzIcons.Filled.Star to stringResource(localesR.string.remove_from_favorites)
            } else {
                MuzIcons.Rounded.Star to stringResource(localesR.string.add_to_favorites)
            }

            val context = LocalContext.current
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
            ) {}
            MuzIconToggleButton(
                checked = song.isFavorite,
                onCheckedChange = { checked ->
                    runCatching {
                        val pendingIntent = MediaStore.createFavoriteRequest(
                            context.contentResolver,
                            listOf(song.mediaUri),
                            checked,
                        )
                        launcher.launch(
                            IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                        )
                    }
                },
                icon = icon,
                contentDescription = contentDescription,
            )
        },
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun LazyGridScope.songs(
    songs: List<Song>,
    nowPlayingState: NowPlayingState,
    onPlaySongsClick: (List<Song>, Int) -> Unit,
    onPlayNextClick: (Song) -> Unit,
) {
    itemsIndexed(
        items = songs,
        key = { _, song -> song.mediaId },
        contentType = { _, _ -> "Song" },
    ) { index, song ->
        val isPlaying = nowPlayingState.mediaId == song.mediaId && nowPlayingState.playWhenReady
        var showSongDetails by rememberSaveable { mutableStateOf(false) }

        SongItem(
            song = song,
            isPlaying = isPlaying,
            modifier = Modifier.animateItem(),
            onClick = { onPlaySongsClick(songs, songs.indexOf(song)) },
            onMenuClick = { showSongDetails = true },
            shapes = ListItemDefaults.segmentedShapes(index, songs.size),
        )

        if (showSongDetails) {
            SongDetailsBottomSheet(
                song = song,
                onDismiss = { showSongDetails = false },
                onPlayNextClick = onPlayNextClick,
            )
        }
    }
}

fun LazyGridScope.songsInfo(
    songs: List<Song>,
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
    ) {
        val count = pluralStringResource(localesR.plurals.number_of_songs, songs.size, songs.size)
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
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .animateItem(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}