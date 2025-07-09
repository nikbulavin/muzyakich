package ru.resodostudio.muzyakich.ui.player

import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.extraSmallContainerSize
import androidx.compose.material3.IconButtonDefaults.largeContainerSize
import androidx.compose.material3.IconButtonDefaults.smallContainerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import ru.resodostudio.muzyakich.core.designsystem.component.MuzOutlinedIconToggleButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.HighQuality
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Star
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.KeyboardArrowDown
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MoreVert
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Pause
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlayArrow
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.QueueMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Repeat
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.RepeatOne
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Shuffle
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.SkipNext
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.SkipPrevious
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Star
import ru.resodostudio.muzyakich.core.designsystem.theme.LocalSharedTransitionScope
import ru.resodostudio.muzyakich.core.designsystem.theme.sharedElementTransitionSpec
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.core.model.data.PlaybackConfig
import ru.resodostudio.muzyakich.core.model.data.RepeatMode
import ru.resodostudio.muzyakich.core.model.data.RepeatMode.REPEAT_ALL
import ru.resodostudio.muzyakich.core.model.data.RepeatMode.REPEAT_OFF
import ru.resodostudio.muzyakich.core.model.data.RepeatMode.REPEAT_ONE
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.ui.util.asFormattedString
import ru.resodostudio.muzyakich.ui.util.convertToProgress
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun PlayerScreen(
    onBackClick: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val playerUiState by viewModel.playerUiState.collectAsStateWithLifecycle()

    PlayerScreen(
        playerUiState = playerUiState,
        onBackClick = onBackClick,
        onSeekTo = viewModel::seekTo,
        onPlayClick = viewModel::play,
        onPauseClick = viewModel::pause,
        onSkipNextClick = viewModel::skipToNext,
        onSkipPreviousClick = viewModel::skipToPrevious,
        onSkipToSongClick = viewModel::skipToSong,
        onShuffleToggle = viewModel::setShuffleModeEnabled,
        onRepeatToggle = viewModel::setRepeatMode,
    )
}

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalSharedTransitionApi::class,
)
@Composable
private fun PlayerScreen(
    playerUiState: PlayerUiState,
    onBackClick: () -> Unit = {},
    onSeekTo: (Float) -> Unit = {},
    onPlayClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onSkipNextClick: () -> Unit = {},
    onSkipPreviousClick: () -> Unit = {},
    onSkipToSongClick: (mediaId: String) -> Unit = {},
    onShuffleToggle: (Boolean) -> Unit = {},
    onRepeatToggle: (RepeatMode) -> Unit = {},
) {
    var queueOpened by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (playerUiState) {
            PlayerUiState.Error -> onBackClick()
            PlayerUiState.Loading -> Unit
            is PlayerUiState.Success -> {
                val currentSong = playerUiState.currentSong
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.statusBarsPadding(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                    BackButton(onBackClick = onBackClick)

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
                                )
                            } else {
                                Column(
                                    modifier = Modifier.requiredHeight(maxHeight / 2 + 80.dp),
                                ) {
                                    Box(
                                        modifier = Modifier.weight(1f),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        SongArtwork(
                                            artworkUri = currentSong.artworkUri,
                                            animatedVisibilityScope = this@AnimatedContent,
                                            modifier = Modifier.padding(horizontal = 24.dp),
                                        )
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 32.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        with(LocalSharedTransitionScope.current) {
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
                                                    MaterialTheme.colorScheme.surface
                                                ),
                                            )
                                        ),
                                )
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
                                            .navigationBarsPadding(),
                                        verticalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        ProgressSlider(
                                            currentPosition = playerUiState.currentPosition,
                                            duration = playerUiState.currentSong.duration,
                                            bitrate = playerUiState.currentSong.bitrate,
                                            onSeekTo = onSeekTo,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                        PlayerActionButtons(
                                            nowPlayingState = playerUiState.nowPlayingState,
                                            onSkipPreviousClick = onSkipPreviousClick,
                                            onPlayClick = onPlayClick,
                                            onPauseClick = onPauseClick,
                                            onSkipNextClick = onSkipNextClick,
                                        )
                                        PlaybackActionButtons(
                                            playbackConfig = playerUiState.playbackConfig,
                                            onRepeatToggle = onRepeatToggle,
                                            onShuffleToggle = onShuffleToggle,
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

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun MoreIconButton(
    modifier: Modifier = Modifier,
) {
    FilledTonalIconButton(
        onClick = {},
        shapes = IconButtonDefaults.shapes(),
        modifier = modifier.size(smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)),
    ) {
        Icon(
            imageVector = MuzIcons.Rounded.MoreVert,
            contentDescription = stringResource(localesR.string.more_options),
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FavoriteToggleButton(
    song: Song,
    modifier: Modifier = Modifier,
) {
    val (icon, contentDescription) = if (song.isFavorite) {
        MuzIcons.Filled.Star to stringResource(localesR.string.remove_from_favorites)
    } else {
        MuzIcons.Rounded.Star to stringResource(localesR.string.add_to_favorites)
    }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
    ) {}
    val hapticFeedback = LocalHapticFeedback.current
    FilledTonalIconToggleButton(
        checked = song.isFavorite,
        onCheckedChange = { checked ->
            hapticFeedback.performHapticFeedback(
                if (checked) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff
            )
            runCatching {
                val pendingIntent = MediaStore.createFavoriteRequest(
                    context.contentResolver,
                    listOf(song.mediaUri),
                    checked,
                )
                launcher.launch(IntentSenderRequest.Builder(pendingIntent.intentSender).build())
            }
        },
        shapes = IconButtonDefaults.toggleableShapes(),
        modifier = modifier,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
        )
    }
}

@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3ExpressiveApi::class,
)
@Composable
private fun SongArtwork(
    artworkUri: Uri,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    with(LocalSharedTransitionScope.current) {
        Crossfade(
            targetState = artworkUri,
            modifier = modifier
                .sharedBounds(
                    boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                    sharedContentState = rememberSharedContentState(artworkUri),
                    animatedVisibilityScope = animatedVisibilityScope,
                )
                .clip(RoundedCornerShape(18.dp)),
            animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec(),
        ) { artworkUriState ->
            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = artworkUriState,
                contentDescription = null,
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
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun BackButton(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedIconButton(
        onClick = onBackClick,
        modifier = modifier
            .padding(top = 8.dp)
            .size(extraSmallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
        shapes = IconButtonDefaults.shapes(IconButtonDefaults.extraSmallRoundShape),
        border = IconButtonDefaults.outlinedIconButtonBorder(true).copy(
            brush = SolidColor(MaterialTheme.colorScheme.outlineVariant),
        ),
    ) {
        Icon(
            imageVector = MuzIcons.Rounded.KeyboardArrowDown,
            contentDescription = stringResource(localesR.string.back),
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun PlaybackActionButtons(
    playbackConfig: PlaybackConfig,
    onRepeatToggle: (RepeatMode) -> Unit,
    onShuffleToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    queueOpened: Boolean = false,
    onQueueClick: (Boolean) -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MuzOutlinedIconToggleButton(
            modifier = Modifier.size(smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
            checked = playbackConfig.shuffleModeEnabled,
            onCheckedChange = onShuffleToggle,
            shape = IconButtonDefaults.smallSquareShape,
            icon = MuzIcons.Rounded.Shuffle,
            contentDescriptionRes = localesR.string.shuffle,
        )

        val repeatMode = playbackConfig.repeatMode
        val icon =
            if (repeatMode == REPEAT_ONE) MuzIcons.Rounded.RepeatOne else MuzIcons.Rounded.Repeat
        val contentDescriptionRes = when (repeatMode) {
            REPEAT_OFF -> localesR.string.enable_repeat_mode_all
            REPEAT_ALL -> localesR.string.enable_repeat_mode_one
            REPEAT_ONE -> localesR.string.disable_repeat_mode
        }
        val hapticFeedback = LocalHapticFeedback.current
        MuzOutlinedIconToggleButton(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
            checked = repeatMode != REPEAT_OFF,
            icon = icon,
            contentDescriptionRes = contentDescriptionRes,
            onCustomCheckedChange = {
                val newRepeatMode = when (repeatMode) {
                    REPEAT_OFF -> {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                        REPEAT_ALL
                    }

                    REPEAT_ALL -> {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                        REPEAT_ONE
                    }

                    REPEAT_ONE -> {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOff)
                        REPEAT_OFF
                    }
                }
                onRepeatToggle(newRepeatMode)
            },
            shape = IconButtonDefaults.smallSquareShape,
        )

        MuzOutlinedIconToggleButton(
            modifier = Modifier.size(smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
            checked = queueOpened,
            icon = MuzIcons.Rounded.QueueMusic,
            contentDescriptionRes = localesR.string.queue,
            onCheckedChange = onQueueClick,
            shape = IconButtonDefaults.smallSquareShape,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun PlayerActionButtons(
    nowPlayingState: NowPlayingState,
    onSkipPreviousClick: () -> Unit,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FilledTonalIconButton(
            onClick = onSkipPreviousClick,
            shapes = IconButtonDefaults.shapes(),
            modifier = Modifier.size(largeContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)),
        ) {
            Icon(
                imageVector = MuzIcons.Rounded.SkipPrevious,
                contentDescription = stringResource(localesR.string.skip_previous),
                modifier = Modifier.size(32.dp),
            )
        }
        FilledIconButton(
            onClick = {
                if (!nowPlayingState.playWhenReady) {
                    onPlayClick()
                } else {
                    onPauseClick()
                }
            },
            shapes = IconButtonDefaults.shapes(),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(largeContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)),
        ) {
            val animSpec = MaterialTheme.motionScheme.slowEffectsSpec<Float>()
            AnimatedContent(
                targetState = !nowPlayingState.playWhenReady,
                label = "PlayPauseButton",
                transitionSpec = {
                    fadeIn(animSpec) + scaleIn(initialScale = 0.3f) togetherWith fadeOut(
                        animSpec
                    ) + scaleOut(targetScale = 0.3f)
                },
            ) { paused ->
                if (paused) {
                    Icon(
                        imageVector = MuzIcons.Rounded.PlayArrow,
                        contentDescription = stringResource(localesR.string.play_audio),
                        modifier = Modifier.size(32.dp),
                    )
                } else {
                    Icon(
                        imageVector = MuzIcons.Rounded.Pause,
                        contentDescription = stringResource(localesR.string.pause_audio),
                        modifier = Modifier.size(32.dp),
                    )
                }
            }
        }
        FilledTonalIconButton(
            onClick = onSkipNextClick,
            shapes = IconButtonDefaults.shapes(),
            enabled = nowPlayingState.hasNextMediaItem,
            modifier = Modifier.size(largeContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)),
        ) {
            Icon(
                imageVector = MuzIcons.Rounded.SkipNext,
                contentDescription = stringResource(localesR.string.skip_next),
                modifier = Modifier.size(32.dp),
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun ProgressSlider(
    currentPosition: Long,
    duration: Long,
    bitrate: Int,
    onSeekTo: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        val progress = convertToProgress(
            count = currentPosition,
            total = duration,
        )
        var sliderPosition by remember { mutableFloatStateOf(0f) }
        var isSeeking by remember { mutableStateOf(false) }
        if (!isSeeking) sliderPosition = progress
        Slider(
            value = sliderPosition,
            onValueChange = {
                isSeeking = true
                sliderPosition = it
                onSeekTo(it)
            },
            onValueChangeFinished = {
                onSeekTo(sliderPosition)
                isSeeking = false
            },
            modifier = Modifier.height(32.dp),
        )

        val timeMillis by remember(currentPosition) {
            derivedStateOf {
                val current = currentPosition / 1000
                val total = duration / 1000
                val remaining = (total - current).coerceAtLeast(0)
                TimeMillis(
                    current = current.asFormattedString(),
                    remaining = remaining.asFormattedString(),
                )
            }
        }

        Box(
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = timeMillis.current,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Text(
                    text = "-${timeMillis.remaining}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            this@Column.AnimatedVisibility(
                visible = bitrate >= 256,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                Icon(
                    imageVector = MuzIcons.Filled.HighQuality,
                    contentDescription = stringResource(localesR.string.high_quality),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private data class TimeMillis(val current: String, val remaining: String)