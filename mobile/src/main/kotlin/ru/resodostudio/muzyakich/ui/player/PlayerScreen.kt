package ru.resodostudio.muzyakich.ui.player

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import coil3.compose.SubcomposeAsyncImage
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.theme.LocalSharedTransitionScope
import ru.resodostudio.muzyakich.core.designsystem.theme.sharedElementTransitionSpec
import ru.resodostudio.muzyakich.ui.component.LoadingState
import ru.resodostudio.muzyakich.ui.util.convertToProgress

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val playerUiState by viewModel.playerUiState.collectAsStateWithLifecycle()

    PlayerScreen(
        playerUiState = playerUiState,
        onSeekTo = viewModel::seekTo,
        onPlay = viewModel::play,
    )
}

@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
private fun PlayerScreen(
    playerUiState: PlayerUiState,
    onSeekTo: (Float) -> Unit = {},
    onPlay: () -> Unit = {},
) {
    val animatedVisibilityScope = LocalNavAnimatedContentScope.current
    val sharedTransitionScope = LocalSharedTransitionScope.current

    with(sharedTransitionScope) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            when (playerUiState) {
                PlayerUiState.Error -> LoadingState()
                PlayerUiState.Loading -> LoadingState()
                is PlayerUiState.Success -> {
                    val song = playerUiState.currentSong
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .sharedBounds(
                                    boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                    sharedContentState = rememberSharedContentState(song.artworkUri),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                )
                                .clip(RoundedCornerShape(18.dp)),
                            model = song.artworkUri,
                            contentDescription = null,
                            error = {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
                                ) {
                                    Icon(
                                        imageVector = MuzIcons.Rounded.MusicNote,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            },
                        )
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 32.dp),
                        ) {
                            Text(
                                text = song.title,
                                maxLines = 1,
                                modifier = Modifier
                                    .sharedBounds(
                                        boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                        sharedContentState = rememberSharedContentState(song.title),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    )
                                    .basicMarquee(),
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Text(
                                text = song.artist,
                                maxLines = 1,
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .sharedBounds(
                                        boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                        sharedContentState = rememberSharedContentState(song.artist),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    )
                                    .basicMarquee(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                            val progress = convertToProgress(
                                count = playerUiState.currentPosition,
                                total = song.duration,
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .sharedBounds(
                                        boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                                        sharedContentState = rememberSharedContentState(song.mediaId),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    ),
                            )
                        }
                    }
                }
            }
        }
    }
}