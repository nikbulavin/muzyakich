package ru.resodostudio.muzyakich.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
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
)
data class PlaylistSongCrossRef(
    @ColumnInfo(name = "playlist_uuid")
    val playlistUuid: Uuid,
    @ColumnInfo(name = "song_uuid", index = true)
    val songUuid: Uuid,
    @ColumnInfo(name = "position")
    val position: Int,
)