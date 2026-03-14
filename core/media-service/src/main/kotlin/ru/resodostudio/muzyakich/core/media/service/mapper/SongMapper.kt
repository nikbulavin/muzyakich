package ru.resodostudio.muzyakich.core.media.service.mapper

import androidx.media3.common.MediaItem
import ru.resodostudio.muzyakich.core.media.service.util.ALBUM_ID
import ru.resodostudio.muzyakich.core.media.service.util.ARTIST_ID
import ru.resodostudio.muzyakich.core.media.service.util.DURATION
import ru.resodostudio.muzyakich.core.media.service.util.FOLDER
import ru.resodostudio.muzyakich.core.media.service.util.UUID
import ru.resodostudio.muzyakich.core.media.service.util.buildPlayableMediaItem
import ru.resodostudio.muzyakich.core.model.data.Song
import kotlin.uuid.Uuid

internal fun Song.asMediaItem() = buildPlayableMediaItem(
    mediaId = mediaId,
    artistId = artistId,
    albumId = albumId,
    mediaUri = mediaUri,
    artworkUri = artworkUri,
    title = title,
    artist = artist,
    folder = path,
    duration = duration,
    trackNumber = trackNumber,
    releaseYear = year,
)

fun MediaItem.asSong(): Song {
    return Song(
        uuid = Uuid.parse(mediaMetadata.extras?.getString(UUID) ?: ""),
        mediaId = mediaId,
        artist = mediaMetadata.artist?.toString() ?: "Unknown",
        title = mediaMetadata.title?.toString() ?: "Unknown",
        mediaUri = requestMetadata.mediaUri!!,
        artworkUri = mediaMetadata.artworkUri!!,
        duration = mediaMetadata.extras?.getLong(DURATION) ?: 0L,
        albumId = mediaMetadata.extras?.getLong(ALBUM_ID) ?: 0L,
        path = mediaMetadata.extras?.getString(FOLDER) ?: "",
        bitrate = 0,
        artistId = mediaMetadata.extras?.getLong(ARTIST_ID) ?: 0L,
        album = mediaMetadata.albumTitle?.toString() ?: "Unknown",
        isFavorite = false,
        size = 0,
        bitsPerSample = 0,
        sampleRate = 0,
        trackNumber = 0,
        year = 0,
        genre = null,
    )
}