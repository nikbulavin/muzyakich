package ru.resodostudio.muzyakich.core.ui

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.HazePerformanceMode
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.blur.hazeBlur
import dev.chrisbanes.haze.blur.materials.HazeMaterials
import ru.resodostudio.muzyakich.core.designsystem.theme.LocalSharedTransitionScope
import ru.resodostudio.muzyakich.core.designsystem.theme.SharedElementKey
import ru.resodostudio.muzyakich.core.designsystem.theme.SharedElementType
import ru.resodostudio.muzyakich.core.designsystem.theme.sharedElementTransitionSpec

@Composable
fun ArtworkBackground(
    artworkUri: Any?,
    sharedElementId: String,
    placeholderIcon: ImageVector,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenWidthPx = with(density) { windowInfo.containerDpSize.width.toPx() }
    val surfaceColor = MaterialTheme.colorScheme.surface
    val surfaceContainerColor = MaterialTheme.colorScheme.surfaceContainer

    val isBlurSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val artworkKey = artworkUri.toString()

    val mainImageRequest = remember(context, artworkUri) {
        ImageRequest.Builder(context)
            .data(artworkUri)
            .placeholderMemoryCacheKey(artworkKey)
            .memoryCacheKey(artworkKey)
            .build()
    }

    val ultraThinStyle = HazeMaterials.ultraThin(surfaceColor)
    val blurStyle = remember(ultraThinStyle, screenWidthPx) {
        ultraThinStyle.then {
            blurEnabled(true)
            blurRadius(50.dp)
            noiseFactor(0f)
            progressive(
                HazeProgressive.verticalGradient(
                    startY = screenWidthPx * 0.65f,
                    endY = screenWidthPx * 1f,
                ),
            )
        }
    }

    with(LocalSharedTransitionScope.current) {
        val captureLayer = rememberGraphicsLayer()
        var isMainSettled by remember(artworkUri) { mutableStateOf(false) }
        var isMirrorSettled by remember(artworkUri) { mutableStateOf(false) }
        var cachedBitmap by remember(artworkUri) { mutableStateOf<ImageBitmap?>(null) }

        val isTransitioning = isTransitionActive

        LaunchedEffect(
            isBlurSupported,
            artworkUri,
            surfaceColor,
            isMainSettled,
            isMirrorSettled,
            isTransitioning,
        ) {
            cachedBitmap = null
            if (isBlurSupported && isMainSettled && isMirrorSettled && !isTransitioning) {
                //delay(100.milliseconds)
                //withFrameNanos { }
                if (captureLayer.size.width > 0 && captureLayer.size.height > 0) {
                    runCatching {
                        cachedBitmap = captureLayer.toImageBitmap()
                    }
                }
            }
        }

        val containerModifier = if (isBlurSupported) {
            modifier
                .drawWithContent {
                    val bitmap = cachedBitmap
                    if (bitmap != null && !isTransitioning) {
                        drawImage(bitmap)
                    } else {
                        captureLayer.record {
                            this@drawWithContent.drawContent()
                        }
                        drawLayer(captureLayer)
                    }
                }
                .hazeBlur(
                    input = HazeInput.Content,
                    style = blurStyle,
                    performanceMode = HazePerformanceMode.Default,
                )
        } else {
            modifier
        }

        Column(
            modifier = containerModifier,
        ) {
            SubcomposeAsyncImage(
                model = mainImageRequest,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                onSuccess = { isMainSettled = true },
                onError = { isMainSettled = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(
                            key = SharedElementKey(
                                id = sharedElementId,
                                origin = artworkKey,
                                type = SharedElementType.Artwork,
                            ),
                        ),
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        boundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
                        renderInOverlayDuringTransition = false,
                    )
                    .then(
                        if (!isBlurSupported) {
                            Modifier.drawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        startY = size.height * 0.65f,
                                        endY = size.height,
                                        colors = listOf(
                                            Color.Transparent,
                                            surfaceContainerColor,
                                        ),
                                    ),
                                )
                            }
                        } else {
                            Modifier
                        }
                    ),
                error = { ArtworkPlaceholder(icon = placeholderIcon) },
            )
            if (isBlurSupported) {
                val mirroredImageRequest = remember(context, artworkUri) {
                    ImageRequest.Builder(context)
                        .data(artworkUri)
                        .placeholderMemoryCacheKey(artworkKey)
                        .memoryCacheKey("${artworkKey}_128")
                        .size(128)
                        .build()
                }
                val bottomOverlayBrush = remember(surfaceColor) {
                    Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        0.45f to surfaceColor.copy(alpha = 0.5f),
                        0.95f to surfaceColor,
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                ) {
                    SubcomposeAsyncImage(
                        model = mirroredImageRequest,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        onSuccess = { isMirrorSettled = true },
                        onError = { isMirrorSettled = true },
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleY = -1f
                            },
                        error = { ArtworkPlaceholder(icon = placeholderIcon) },
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(bottomOverlayBrush),
                    )
                }
            } else {
                val fallbackBrush = remember(surfaceContainerColor, surfaceColor) {
                    Brush.verticalGradient(
                        0.0f to surfaceContainerColor,
                        0.75f to surfaceColor,
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(fallbackBrush),
                )
            }
        }
    }
}

@Composable
private fun ArtworkPlaceholder(
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(0.35f),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
