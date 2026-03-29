package ru.resodostudio.muzyakich.core.media.service.util

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.RequestMetadata
import androidx.media3.common.MediaMetadata

internal fun buildPlayableMediaItem(
    mediaId: String,
    mediaUri: Uri,
    artworkUri: Uri,
    title: String,
    artist: String,
) = MediaItem.Builder()
    .setMediaId(mediaId)
    .setRequestMetadata(
        RequestMetadata.Builder()
            .setMediaUri(mediaUri)
            .build()
    )
    .setMediaMetadata(
        MediaMetadata.Builder()
            .setArtworkUri(artworkUri)
            .setTitle(title)
            .setArtist(artist)
            .setIsBrowsable(false)
            .setIsPlayable(true)
            .build()
    )
    .build()

internal const val UUID = "uuid"
internal const val ARTIST_ID = "artist_id"
internal const val ALBUM_ID = "album_id"
internal const val FOLDER = "folder"
internal const val DURATION = "duration"