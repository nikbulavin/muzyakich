package ru.resodostudio.muzyakich.core.ui.util

import android.net.Uri
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.materialkolor.ktx.animateColorScheme
import com.materialkolor.ktx.rememberThemeColor
import com.materialkolor.rememberDynamicColorScheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DynamicPlayerTheme(
    artworkUri: Uri?,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val fallbackScheme = MaterialTheme.colorScheme
    val imageBitmap = rememberArtworkImageBitmap(artworkUri)
    val targetScheme = if (artworkUri != null && imageBitmap != null) {
        val seedColor = rememberThemeColor(image = imageBitmap, fallback = fallbackScheme.primary)
        rememberDynamicColorScheme(
            seedColor = seedColor,
            isDark = isDarkTheme,
        )
    } else {
        fallbackScheme
    }

    MaterialExpressiveTheme(
        colorScheme = animateColorScheme(targetScheme),
        typography = MaterialTheme.typography,
        motionScheme = MaterialTheme.motionScheme,
        shapes = MaterialTheme.shapes,
        content = content,
    )
}

@Composable
private fun rememberArtworkImageBitmap(artworkUri: Uri?): ImageBitmap? {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(artworkUri) {
        if (artworkUri == null) {
            imageBitmap = null
            return@LaunchedEffect
        }
        withContext(Dispatchers.IO) {
            val request = ImageRequest.Builder(context)
                .data(artworkUri)
                .size(64)
                .allowHardware(false)
                .build()

            val result = context.imageLoader.execute(request)
            if (result is SuccessResult) {
                imageBitmap = result.image.toBitmap().asImageBitmap()
            }
        }
    }
    return imageBitmap
}
