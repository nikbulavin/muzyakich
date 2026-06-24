package ru.resodostudio.muzyakich.core.database.model

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "songs",
    indices = [
        Index("media_id"),
    ],
)
data class SongEntity(
    @PrimaryKey
    val uuid: Uuid,
    @ColumnInfo(name = "media_id")
    val mediaId: String,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,
    @ColumnInfo(name = "play_count")
    val playCount: Int,
    @ColumnInfo(name = "last_played")
    val lastPlayed: Instant?,
)