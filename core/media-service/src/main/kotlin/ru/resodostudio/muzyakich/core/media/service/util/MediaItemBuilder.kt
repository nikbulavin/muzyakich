package ru.resodostudio.muzyakich.core.media.service.util

import android.net.Uri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.RequestMetadata
import androidx.media3.common.MediaMetadata
import kotlin.uuid.Uuid

internal fun buildPlayableMediaItem(
    mediaId: String,
    artistId: Long,
    albumId: Long,
    mediaUri: Uri,
    artworkUri: Uri,
    title: String,
    artist: String,
    folder: String,
    duration: Long,
    trackNumber: Int,
    releaseYear: Int,
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
            .setTrackNumber(trackNumber)
            .setReleaseYear(releaseYear)
            .setExtras(
                bundleOf(
                    UUID to Uuid.random().toString(),
                    ARTIST_ID to artistId,
                    ALBUM_ID to albumId,
                    FOLDER to folder,
                    DURATION to duration,
                )
            )
            .build()
    )
    .build()

internal const val UUID = "uuid"
internal const val ARTIST_ID = "artist_id"
internal const val ALBUM_ID = "album_id"
internal const val FOLDER = "folder"
internal const val DURATION = "duration"