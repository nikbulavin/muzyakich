package ru.resodostudio.muzyakich.core.model

import kotlin.uuid.Uuid

data class PlaylistSong(
    val uuid: Uuid,
    val song: Song,
)
