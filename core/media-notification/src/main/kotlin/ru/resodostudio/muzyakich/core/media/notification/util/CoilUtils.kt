package ru.resodostudio.muzyakich.core.media.notification.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.error
import coil3.request.placeholder
import coil3.toBitmap
import ru.resodostudio.muzyakich.core.media.notification.R

internal suspend fun Uri.asArtworkBitmap(context: Context): Bitmap? {
    val loader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(this)
        .placeholder(R.drawable.ic_placeholder)
        .error(R.drawable.ic_placeholder)
        .build()

    return loader.execute(request).image?.toBitmap()
}