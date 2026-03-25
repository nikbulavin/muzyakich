package ru.resodostudio.muzyakich.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "playlists",
)
data class PlaylistEntity(
    @PrimaryKey
    val uuid: Uuid,
    val title: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Instant,
    @ColumnInfo(name = "cover_file_path")
    val coverFilePath: String?,
)