package ru.resodostudio.muzyakich.core.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Playlist(
    val uuid: Uuid,
    val title: String,
    val timestamp: Instant,
    val coverFilePath: String?,
    val songs: List<Song>,
)