package ru.resodostudio.muzyakich.core.media.service.util

import androidx.media3.common.MediaItem
import ru.resodostudio.muzyakich.core.model.QueueSong
import ru.resodostudio.muzyakich.core.model.Song

internal fun Song.asMediaItem(): MediaItem {
    return buildPlayableMediaItem(
        mediaId = mediaId,
        mediaUri = mediaUri,
        artworkUri = artworkUri,
        title = title,
        artist = artist,
    )
}

internal fun MediaItem.asQueueSong(uid: String): QueueSong {
    return QueueSong(
        uid = uid,
        mediaId = mediaId,
        artist = mediaMetadata.artist?.toString() ?: "Unknown",
        artworkUri = mediaMetadata.artworkUri!!,
        title = mediaMetadata.title?.toString() ?: "Unknown",
    )
}