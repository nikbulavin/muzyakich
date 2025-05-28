package ru.resodostudio.muzyakich.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.SubcomposeAsyncImage
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import ru.resodostudio.muzyakich.R
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MoreVert
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun SongItem(
    song: Song,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    onClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        headlineContent = {
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
                            .background(MaterialTheme.colorScheme.surface.copy(0.6f))
                            .clip(RoundedCornerShape(8.dp)),
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
                        .clip(RoundedCornerShape(8.dp)),
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
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    },
                    filterQuality = FilterQuality.Low,
                )
            }
        },
        trailingContent = {
            IconButton(
                onClick = onMenuClick,
            ) {
                Icon(
                    imageVector = MuzIcons.Rounded.MoreVert,
                    contentDescription = stringResource(localesR.string.open_menu),
                )
            }
        },
    )
}