package ru.resodostudio.muzyakich.core.media.notification.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.error
import coil3.toBitmap
import ru.resodostudio.muzyakich.core.media.notification.R

internal suspend fun Uri.asArtworkBitmap(context: Context): Bitmap? {
    return ImageLoader(context).execute(
        ImageRequest.Builder(context)
            .data(this)
            .error(R.drawable.ic_placeholder)
            .build()
    ).image?.toBitmap()
}