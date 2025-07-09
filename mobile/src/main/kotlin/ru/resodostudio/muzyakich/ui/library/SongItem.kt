package ru.resodostudio.muzyakich.ui.library

import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.smallContainerSize
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
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
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Star
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MoreVert
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Star
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
                            .clip(MaterialTheme.shapes.small),
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                IconToggleButton(
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
                            launcher.launch(
                                IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                            )
                        }
                    },
                    shapes = IconButtonDefaults.toggleableShapes(),
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDescription,
                    )
                }
                IconButton(
                    onClick = onMenuClick,
                    shapes = IconButtonDefaults.shapes(),
                    modifier = Modifier.size(smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow)),
                ) {
                    Icon(
                        imageVector = MuzIcons.Rounded.MoreVert,
                        contentDescription = stringResource(localesR.string.open_menu),
                    )
                }
            }
        },
    )
}