package ru.resodostudio.muzyakich.core.database.model

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import kotlin.uuid.Uuid

@Entity(
    tableName = "playlist_songs",
    primaryKeys = ["playlist_uuid", "song_uuid"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["playlist_uuid"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["song_uuid"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("song_uuid"),
    ],
)
data class PlaylistSongCrossRef(
    @ColumnInfo(name = "playlist_uuid")
    val playlistUuid: Uuid,
    @ColumnInfo(name = "song_uuid")
    val songUuid: Uuid,
    @ColumnInfo(name = "position")
    val position: Int,
)