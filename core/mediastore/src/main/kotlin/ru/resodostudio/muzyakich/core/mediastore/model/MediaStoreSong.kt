package ru.resodostudio.muzyakich.core.mediastore.model

import android.net.Uri
import ru.resodostudio.muzyakich.core.model.data.Song
import kotlin.uuid.Uuid

data class MediaStoreSong(
    val mediaId: String,
    val artistId: Long,
    val albumId: Long,
    val mediaUri: Uri,
    val artworkUri: Uri,
    val title: String,
    val artist: String,
    val album: String,
    val path: String,
    val duration: Long,
    val bitrate: Int,
    val size: Int,
    val bitsPerSample: Int?,
    val sampleRate: Int?,
    val trackNumber: Int,
    val year: Int,
    val genre: String?,
)

fun MediaStoreSong.asExternalModel(
    isFavorite: Boolean,
): Song {
    return Song(
        isFavorite = isFavorite,
        uuid = Uuid.random(),
        mediaId = mediaId,
        artistId = artistId,
        albumId = albumId,
        mediaUri = mediaUri,
        artworkUri = artworkUri,
        title = title,
        artist = artist,
        album = album,
        path = path,
        duration = duration,
        bitrate = bitrate,
        size = size,
        bitsPerSample = bitsPerSample,
        sampleRate = sampleRate,
        trackNumber = trackNumber,
        year = year,
        genre = genre,
    )
}