package ru.resodostudio.muzyakich.core.model

data class QueueSong(
    val uid: String,
    val mediaId: String,
    val artist: String,
    val artworkUri: String,
    val title: String,
)
