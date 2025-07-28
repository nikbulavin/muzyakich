package ru.resodostudio.muzyakich.ui.component

import android.net.Uri
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.designsystem.theme.LocalSharedTransitionScope
import ru.resodostudio.muzyakich.core.designsystem.theme.sharedElementTransitionSpec

@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3ExpressiveApi::class,
)
@Composable
fun SongArtworkMini(
    artworkUri: Uri,
    size: Dp,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    with(LocalSharedTransitionScope.current) {
        SubcomposeAsyncImage(
            modifier = modifier
                .size(size)
                .dropShadow(
                    shape = shape,
                    shadow = Shadow(
                        radius = 3.dp,
                        color = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.2f)
                    ),
                )
                .then(
                    if (animatedVisibilityScope != null) {
                        Modifier.sharedBounds(
                            boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                            sharedContentState = rememberSharedContentState(artworkUri.toString()),
                            animatedVisibilityScope = animatedVisibilityScope,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                        )
                    } else {
                        Modifier
                    }
                )
                .clip(shape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(artworkUri)
                .crossfade(true)
                .size(256)
                .placeholderMemoryCacheKey(artworkUri.toString())
                .memoryCacheKey(artworkUri.toString())
                .build(),
            contentDescription = null,
            error = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(size)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Icon(
                        imageVector = MuzIcons.Rounded.MusicNote,
                        contentDescription = null,
                        modifier = Modifier.size((size.value / 1.75).dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
        )
    }
}