package ru.resodostudio.muzyakich.ui.player

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.smallContainerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.ui.compose.indicators.TimeText
import androidx.media3.ui.compose.material3.indicator.ProgressSlider
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledTonalIconButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledTonalIconToggleButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Star
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.HighQuality
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MoreVert
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Star
import ru.resodostudio.muzyakich.core.designsystem.theme.sharedElementTransitionSpec
import ru.resodostudio.muzyakich.core.model.data.Song
import kotlin.uuid.Uuid
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun PlayerScreen(
    onDismiss: () -> Unit,
    onSongMenuClick: (String) -> Unit,
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val playerUiState by viewModel.playerUiState.collectAsStateWithLifecycle()

    PlayerScreen(
        playerUiState = playerUiState,
        onDismiss = onDismiss,
        onSongMenuClick = onSongMenuClick,
        onSkipToSongClick = viewModel::skipToSong,
        onFavoriteChange = viewModel::setSongFavorite,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PlayerScreen(
    playerUiState: PlayerUiState,
    onDismiss: () -> Unit,
    onSongMenuClick: (String) -> Unit,
    onSkipToSongClick: (Uuid) -> Unit = {},
    onFavoriteChange: (String, Boolean) -> Unit = { _, _ -> },
) {
    SharedTransitionLayout {
        var queueOpened by rememberSaveable { mutableStateOf(false) }
        when (playerUiState) {
            PlayerUiState.Error -> onDismiss()
            PlayerUiState.Loading -> Unit
            is PlayerUiState.Success -> {
                val currentSong = playerUiState.currentSong
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        val lazyListState = rememberLazyListState()
                        val isPlayerActionsVisible by remember {
                            derivedStateOf { lazyListState.firstVisibleItemIndex == 0 }
                        }
                        val animSpec = MaterialTheme.motionScheme.defaultEffectsSpec<Float>()
                        AnimatedContent(
                            targetState = queueOpened,
                            transitionSpec = { fadeIn(animSpec) togetherWith fadeOut(animSpec) },
                        ) { queueOpenedState ->
                            if (queueOpenedState) {
                                QueuePanel(
                                    lazyListState = lazyListState,
                                    currentSong = currentSong,
                                    playingQueue = playerUiState.nowPlayingState.playingQueue,
                                    modifier = Modifier.padding(top = 16.dp),
                                    animatedVisibilityScope = this,
                                    onQueueItemClick = onSkipToSongClick,
                                    onFavoriteChange = onFavoriteChange,
                                    onSongLongClick = onSongMenuClick,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                )
                            } else {
                                Column(
                                    modifier = Modifier.requiredHeight(maxHeight / 2 + 80.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        SongArtwork(
                                            artworkUri = currentSong.artworkUri,
                                            animatedVisibilityScope = this@AnimatedContent,
                                            sharedTransitionScope = this@SharedTransitionLayout,
                                        )
                                    }
                                    Spacer(Modifier.height(16.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 32.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
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
                                                        sharedContentState = rememberSharedContentState(
                                                            currentSong.title
                                                        ),
                                                        animatedVisibilityScope = this@AnimatedContent,
                                                    )
                                                    .basicMarquee(),
                                                style = MaterialTheme.typography.titleLarge,
                                            )
                                            Text(
                                                text = currentSong.artist,
                                                maxLines = 1,
                                                modifier = Modifier
                                                    .sharedBounds(
                                                        boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                                        sharedContentState = rememberSharedContentState(
                                                            currentSong.artist
                                                        ),
                                                        animatedVisibilityScope = this@AnimatedContent,
                                                    )
                                                    .basicMarquee(),
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            )
                                        }
                                        FavoriteToggleButton(
                                            song = currentSong,
                                            onFavoriteChange = onFavoriteChange,
                                            modifier = Modifier
                                                .sharedBounds(
                                                    boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                                    sharedContentState = rememberSharedContentState(
                                                        localesR.string.favorites
                                                    ),
                                                    animatedVisibilityScope = this@AnimatedContent,
                                                ),
                                        )
                                        MoreIconButton(
                                            onClick = { onSongMenuClick(currentSong.mediaId) },
                                            modifier = Modifier
                                                .sharedBounds(
                                                    boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                                    sharedContentState = rememberSharedContentState(
                                                        localesR.string.more_options
                                                    ),
                                                    animatedVisibilityScope = this@AnimatedContent,
                                                ),
                                        )
                                    }
                                }
                            }
                        }

                        val spatialSpec = MaterialTheme.motionScheme.defaultSpatialSpec<IntSize>()
                        val effectsSpec = MaterialTheme.motionScheme.defaultEffectsSpec<Float>()
                        this@Column.AnimatedVisibility(
                            visible = isPlayerActionsVisible || !queueOpened,
                            modifier = Modifier.align(Alignment.BottomCenter),
                            enter = fadeIn(effectsSpec) + expandVertically(spatialSpec),
                            exit = fadeOut(effectsSpec) + shrinkVertically(spatialSpec),
                        ) {
                            Column(
                                modifier = Modifier.requiredHeight(maxHeight / 2 - 80.dp),
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .background(
                                            brush = Brush.verticalGradient(
                                                listOf(
                                                    Color.Transparent,
                                                    MaterialTheme.colorScheme.surfaceContainerLow,
                                                ),
                                            )
                                        ),
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                        .padding(start = 32.dp, end = 32.dp, bottom = 16.dp),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    playerUiState.nowPlayingState.player?.let { player ->
                                        ProgressSlider(
                                            player = player,
                                            bitrate = playerUiState.currentSong.bitrate,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    }
                                    playerUiState.nowPlayingState.player?.let { player ->
                                        PlayerControlButtonGroup(
                                            player = player,
                                        )
                                        PlaybackButtonGroup(
                                            player = player,
                                            queueOpened = queueOpened,
                                            onQueueClick = { queueOpened = it },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MoreIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MuzFilledTonalIconButton(
        modifier = modifier,
        onClick = dropUnlessResumed { onClick() },
        containerSize = smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow),
        icon = MuzIcons.Rounded.MoreVert,
        contentDescription = stringResource(localesR.string.open_menu),
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FavoriteToggleButton(
    song: Song,
    onFavoriteChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (icon, contentDescription) = if (song.isFavorite) {
        MuzIcons.Filled.Star to stringResource(localesR.string.remove_from_favorites)
    } else {
        MuzIcons.Rounded.Star to stringResource(localesR.string.add_to_favorites)
    }
    MuzFilledTonalIconToggleButton(
        checked = song.isFavorite,
        onCheckedChange = { onFavoriteChange(song.mediaId, it) },
        modifier = modifier,
        icon = icon,
        contentDescription = contentDescription,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SongArtwork(
    artworkUri: Uri,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier,
) {
    with(sharedTransitionScope) {
        SubcomposeAsyncImage(
            modifier = modifier
                .padding(horizontal = 24.dp)
                .aspectRatio(1f)
                .dropShadow(
                    shape = MaterialTheme.shapes.large,
                    shadow = Shadow(
                        radius = 14.dp,
                        spread = 6.dp,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        alpha = 0.1f,
                    ),
                )
                .sharedBounds(
                    boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                    sharedContentState = rememberSharedContentState(artworkUri.toString()),
                    animatedVisibilityScope = animatedVisibilityScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                )
                .clip(MaterialTheme.shapes.large),
            model = ImageRequest.Builder(LocalContext.current)
                .data(artworkUri)
                .crossfade(true)
                .placeholderMemoryCacheKey(artworkUri.toString())
                .memoryCacheKey(artworkUri.toString())
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            error = {
                BoxWithConstraints(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                ) {
                    Icon(
                        imageVector = MuzIcons.Rounded.MusicNote,
                        contentDescription = null,
                        modifier = Modifier.size((maxWidth.value / 1.75).dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
        )
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun ProgressSlider(
    player: Player,
    bitrate: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        ProgressSlider(
            player = player,
            modifier = Modifier.height(32.dp),
        )
        TimeText(player) {
            val currentPosition = Util.getStringForTime(currentPositionMs)
                .removePrefix("0")
            val remainingDuration = Util.getStringForTime(durationMs - currentPositionMs)
                .removePrefix("0")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = currentPosition,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                AnimatedVisibility(
                    visible = bitrate >= 256,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                ) {
                    Icon(
                        imageVector = MuzIcons.Rounded.HighQuality,
                        contentDescription = stringResource(localesR.string.high_quality),
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = "-$remainingDuration",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}