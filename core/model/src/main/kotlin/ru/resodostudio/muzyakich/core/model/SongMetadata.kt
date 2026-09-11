package ru.resodostudio.muzyakich.core.model

data class SongMetadata(
    val title: String,
    val artist: String,
    val album: String,
    val albumArtist: String,
    val year: String,
    val genre: String,
    val trackNumber: String,
    val discNumber: String,
    val comment: String,
    val artworkUri: String?,
)
