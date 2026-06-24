package ru.resodostudio.muzyakich.core.database.model

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
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