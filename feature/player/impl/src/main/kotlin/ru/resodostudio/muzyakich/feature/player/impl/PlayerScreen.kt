package ru.resodostudio.muzyakich.feature.player.impl

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.ui.compose.indicators.TimeText
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.theme.sharedElementTransitionSpec
import ru.resodostudio.muzyakich.core.model.QueueSong
import ru.resodostudio.muzyakich.core.model.Song
import ru.resodostudio.muzyakich.feature.player.impl.component.FavoriteToggleButton
import ru.resodostudio.muzyakich.feature.player.impl.component.MoreIconButton
import ru.resodostudio.muzyakich.feature.player.impl.component.PlaybackButtonGroup
import ru.resodostudio.muzyakich.feature.player.impl.component.PlayerControlButtonGroup
import ru.resodostudio.muzyakich.feature.player.impl.component.WavyProgressSlider
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun PlayerScreen(
    onDismiss: () -> Unit,
    onSongMenuClick: (String) -> Unit,
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val playerUiState by viewModel.playerUiState.collectAsStateWithLifecycle()
    val player by viewModel.player.collectAsStateWithLifecycle()
    val activity = LocalActivity.current

    PlayerScreen(
        playerUiState = playerUiState,
        player = player,
        onDismiss = onDismiss,
        onSongMenuClick = onSongMenuClick,
        onSkipToSongClick = viewModel::skipToSong,
        onFavoriteChange = { id, favorite ->
            activity?.let { viewModel.setSongFavorite(id, favorite, it) }
        },
        onRemoveFromQueue = viewModel::removeSong,
        onReorderSongs = viewModel::moveSong,
    )
}

@Composable
private fun PlayerScreen(
    playerUiState: PlayerUiState,
    player: Player?,
    onDismiss: () -> Unit,
    onSongMenuClick: (String) -> Unit,
    onSkipToSongClick: (String) -> Unit = {},
    onFavoriteChange: (String, Boolean) -> Unit = { _, _ -> },
    onRemoveFromQueue: (String) -> Unit = {},
    onReorderSongs: (String, String) -> Unit = { _, _ -> },
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
                        val navBarHeight = WindowInsets.navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                        val safeHeight = maxHeight - navBarHeight
                        val topHeight = (safeHeight / 2) + 80.dp
                        val bottomHeight = (safeHeight / 2) - 80.dp + navBarHeight

                        val lazyListState = rememberLazyListState()
                        val isQueueScrolled by remember { derivedStateOf { lazyListState.lastScrolledForward } }

                        Header(
                            currentSong = currentSong,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            lazyListState = lazyListState,
                            queueOpened = queueOpened,
                            onSkipToSongClick = onSkipToSongClick,
                            onFavoriteChange = onFavoriteChange,
                            onSongMenuClick = onSongMenuClick,
                            onRemoveFromQueue = onRemoveFromQueue,
                            onReorderSongs = onReorderSongs,
                            height = topHeight,
                            playingQueue = playerUiState.playingQueue,
                            playWhenReady = playerUiState.playWhenReady,
                        )

                        Body(
                            player = player,
                            queueOpened = queueOpened,
                            isQueueScrolled = isQueueScrolled,
                            isQueueEmpty = playerUiState.playingQueue.isEmpty(),
                            playWhenReady = playerUiState.playWhenReady,
                            height = bottomHeight,
                            onQueueClick = { queueOpened = it },
                            modifier = Modifier.align(Alignment.BottomCenter),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(
    currentSong: Song,
    sharedTransitionScope: SharedTransitionScope,
    lazyListState: LazyListState,
    queueOpened: Boolean,
    onSkipToSongClick: (String) -> Unit,
    onFavoriteChange: (String, Boolean) -> Unit,
    onSongMenuClick: (String) -> Unit,
    onRemoveFromQueue: (String) -> Unit,
    onReorderSongs: (String, String) -> Unit,
    playingQueue: List<QueueSong>,
    playWhenReady: Boolean,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    with(sharedTransitionScope) {
        val motionScheme = MaterialTheme.motionScheme
        val fadeSpec = motionScheme.defaultEffectsSpec<Float>()
        AnimatedContent(
            targetState = queueOpened,
            transitionSpec = { fadeIn(fadeSpec) togetherWith fadeOut(fadeSpec) },
            label = "QueuePanel",
            modifier = modifier,
        ) { queueOpenedState ->
            val animatedVisibilityScope = this
            if (queueOpenedState) {
                QueuePanel(
                    lazyListState = lazyListState,
                    currentSong = currentSong,
                    playingQueue = playingQueue,
                    modifier = Modifier.padding(top = 16.dp),
                    animatedVisibilityScope = this,
                    onQueueItemClick = onSkipToSongClick,
                    onFavoriteChange = onFavoriteChange,
                    onSongLongClick = onSongMenuClick,
                    onRemoveFromQueue = onRemoveFromQueue,
                    onReorderSongs = onReorderSongs,
                    sharedTransitionScope = sharedTransitionScope,
                )
            } else {
                Column(
                    modifier = Modifier.requiredHeight(height),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        SongArtwork(
                            artworkUri = currentSong.artworkUri,
                            playWhenReady = playWhenReady,
                            animatedVisibilityScope = this@AnimatedContent,
                            sharedTransitionScope = sharedTransitionScope,
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
                            AnimatedContent(
                                targetState = currentSong.title,
                                transitionSpec = {
                                    fadeIn(fadeSpec) +
                                            slideInHorizontally(motionScheme.fastSpatialSpec()) { it / 8 } togetherWith
                                            fadeOut(snap())
                                },
                                label = "TitleAnimation",
                                modifier = Modifier.fillMaxWidth(),
                            ) { title ->
                                Text(
                                    text = title,
                                    maxLines = 1,
                                    modifier = Modifier
                                        .sharedBounds(
                                            boundsTransform = motionScheme.sharedElementTransitionSpec,
                                            sharedContentState = rememberSharedContentState(title),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                        )
                                        .basicMarquee(),
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                            AnimatedContent(
                                targetState = currentSong.artist,
                                transitionSpec = {
                                    val delay = 50
                                    fadeIn(tween(300, delay)) +
                                            slideInHorizontally(tween(300, delay)) { it / 8 } togetherWith
                                            fadeOut(snap(delay))
                                },
                                label = "ArtistAnimation",
                                modifier = Modifier.fillMaxWidth(),
                            ) { artist ->
                                Text(
                                    text = artist,
                                    maxLines = 1,
                                    modifier = Modifier
                                        .sharedBounds(
                                            boundsTransform = motionScheme.sharedElementTransitionSpec,
                                            sharedContentState = rememberSharedContentState(artist),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                        )
                                        .basicMarquee(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }

                        FavoriteToggleButton(
                            song = currentSong,
                            onFavoriteChange = onFavoriteChange,
                            modifier = Modifier
                                .sharedBounds(
                                    boundsTransform = motionScheme.sharedElementTransitionSpec,
                                    sharedContentState = rememberSharedContentState(
                                        localesR.string.core_locales_favorites,
                                    ),
                                    animatedVisibilityScope = this@AnimatedContent,
                                ),
                        )
                        MoreIconButton(
                            onClick = { onSongMenuClick(currentSong.mediaId) },
                            modifier = Modifier
                                .sharedBounds(
                                    boundsTransform = motionScheme.sharedElementTransitionSpec,
                                    sharedContentState = rememberSharedContentState(
                                        localesR.string.core_locales_more_options,
                                    ),
                                    animatedVisibilityScope = this@AnimatedContent,
                                ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Body(
    player: Player?,
    queueOpened: Boolean,
    isQueueScrolled: Boolean,
    isQueueEmpty: Boolean,
    playWhenReady: Boolean,
    height: Dp,
    onQueueClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val motionScheme = MaterialTheme.motionScheme
    val spatialSpec = motionScheme.defaultSpatialSpec<IntSize>()
    val effectsSpec = motionScheme.defaultEffectsSpec<Float>()
    AnimatedVisibility(
        visible = !isQueueScrolled || !queueOpened || isQueueEmpty,
        modifier = modifier,
        enter = fadeIn(effectsSpec) + expandVertically(spatialSpec),
        exit = fadeOut(effectsSpec) + shrinkVertically(spatialSpec),
    ) {
        Column(
            modifier = Modifier.requiredHeight(height),
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
                        ),
                    ),
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .navigationBarsPadding()
                    .padding(
                        start = 32.dp,
                        end = 32.dp,
                        bottom = 16.dp,
                    ),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SongProgressSection(
                    player = player,
                    playWhenReady = playWhenReady,
                    modifier = Modifier.fillMaxWidth(),
                )
                PlayerControlButtonGroup(
                    player = player,
                    modifier = Modifier.fillMaxWidth(),
                )
                PlaybackButtonGroup(
                    player = player,
                    queueOpened = queueOpened,
                    onQueueClick = onQueueClick,
                )
            }
        }
    }
}

@Composable
private fun SongArtwork(
    artworkUri: String,
    playWhenReady: Boolean,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
) {
    val horizontalPadding by animateDpAsState(
        targetValue = if (playWhenReady) 24.dp else 48.dp,
        animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec(),
        label = "ArtworkScale",
    )

    with(sharedTransitionScope) {
        SubcomposeAsyncImage(
            modifier = modifier
                .padding(horizontal = horizontalPadding)
                .aspectRatio(1f)
                .dropShadow(
                    shape = shape,
                    shadow = Shadow(
                        radius = 14.dp,
                        spread = 6.dp,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        alpha = 0.1f,
                    ),
                )
                .sharedBounds(
                    boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                    sharedContentState = rememberSharedContentState(artworkUri),
                    animatedVisibilityScope = animatedVisibilityScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                )
                .clip(shape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(artworkUri)
                .placeholderMemoryCacheKey(artworkUri)
                .memoryCacheKey(artworkUri)
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
private fun SongProgressSection(
    player: Player?,
    playWhenReady: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        WavyProgressSlider(
            player = player,
            playWhenReady = playWhenReady,
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
                Text(
                    text = "-$remainingDuration",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
