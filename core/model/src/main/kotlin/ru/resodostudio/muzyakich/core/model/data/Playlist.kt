package ru.resodostudio.muzyakich.core.model.data

import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Playlist(
    val uuid: Uuid,
    val name: String,
    val timestamp: Instant,
    val coverFileName: String?,
    val songs: List<Song>,
)