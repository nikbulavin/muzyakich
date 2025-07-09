package ru.resodostudio.muzyakich.ui.player

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.theme.LocalSharedTransitionScope
import ru.resodostudio.muzyakich.core.designsystem.theme.sharedElementTransitionSpec
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.ui.component.SongArtworkMini
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalSharedTransitionApi::class,
)
@Composable
fun QueuePanel(
    currentSong: Song,
    playingQueue: List<Song>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    onQueueItemClick: (String) -> Unit = {},
) {
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
            with(LocalSharedTransitionScope.current) {
                SongArtworkMini(
                    artworkUri = currentSong.artworkUri,
                    size = 64.dp,
                    animatedVisibilityScope = animatedVisibilityScope,
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
                    modifier = Modifier
                        .sharedBounds(
                            boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                            sharedContentState = rememberSharedContentState(localesR.string.favorites),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                )
                MoreIconButton(
                    modifier = Modifier
                        .sharedBounds(
                            boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                            sharedContentState = rememberSharedContentState(localesR.string.more_options),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                )
            }
        }
        Text(
            text = stringResource(localesR.string.next_in_queue),
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 14.dp,
                end = 14.dp,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
            )
        ) {
            items(
                items = playingQueue,
                key = { it.mediaId },
            ) { song ->
                QueueItem(
                    song = song,
                    modifier = Modifier.animateItem(),
                    onClick = { onQueueItemClick(song.mediaId) },
                )
            }
        }
    }
}