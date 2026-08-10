package ru.resodostudio.muzyakich.core.database.model

import androidx.room3.ColumnInfo
import kotlin.uuid.Uuid

data class PlaylistSongLocation(
    @ColumnInfo(name = "playlist_uuid")
    val playlistUuid: Uuid,
    @ColumnInfo(name = "position")
    val position: Int,
)
