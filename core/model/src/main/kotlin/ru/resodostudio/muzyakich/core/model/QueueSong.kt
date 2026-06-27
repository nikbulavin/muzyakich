package ru.resodostudio.muzyakich.core.model

import android.net.Uri

data class QueueSong(
    val uid: String,
    val mediaId: String,
    val artist: String,
    val artworkUri: Uri,
    val title: String,
)
