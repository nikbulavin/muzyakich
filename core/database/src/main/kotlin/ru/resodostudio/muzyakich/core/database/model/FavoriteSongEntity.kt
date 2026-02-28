package ru.resodostudio.muzyakich.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_songs",
)
data class FavoriteSongEntity(
    @PrimaryKey
    @ColumnInfo(name = "media_id")
    val mediaId: String,
)