package ru.resodostudio.muzyakich.core.model

data class Song(
    val mediaId: String,
    val artistId: Long,
    val albumId: Long,
    val mediaUri: String,
    val artworkUri: String,
    val title: String,
    val artist: String,
    val album: String,
    val path: String,
    val duration: Long,
    val bitrate: Int,
    val isFavorite: Boolean,
    val playCount: Int,
    val size: Int,
    val bitsPerSample: Int?,
    val sampleRate: Int?,
    val trackNumber: Int,
    val year: Int,
    val genre: String?,
)
