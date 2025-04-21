package ru.resodostudio.muzyakich.core.media.service.mapper

import ru.resodostudio.muzyakich.core.media.service.util.buildPlayableMediaItem
import ru.resodostudio.muzyakich.core.model.data.Song

internal fun Song.asMediaItem() = buildPlayableMediaItem(
    mediaId = mediaId,
    artistId = artistId,
    albumId = albumId,
    mediaUri = mediaUri,
    artworkUri = artworkUri,
    title = title,
    artist = artist,
    folder = folder,
    duration = duration,
)