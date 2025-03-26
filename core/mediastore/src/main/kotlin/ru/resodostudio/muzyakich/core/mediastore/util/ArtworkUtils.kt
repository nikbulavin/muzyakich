package ru.resodostudio.muzyakich.core.mediastore.util

import android.content.ContentUris
import android.net.Uri
import androidx.core.net.toUri

internal fun Long.asArtworkUri() = ContentUris.withAppendedId(ALBUM_ART_URI.toUri(), this)

private const val ALBUM_ART_URI = "content://media/external/audio/albumart"
