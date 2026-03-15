package ru.resodostudio.muzyakich.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "songs",
)
data class SongEntity(
    @PrimaryKey
    val uuid: Uuid,
    @ColumnInfo(name = "media_id", index = true)
    val mediaId: String,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,
    @ColumnInfo(name = "play_count")
    val playCount: Int,
    @ColumnInfo(name = "last_played")
    val lastPlayed: Instant?,
)