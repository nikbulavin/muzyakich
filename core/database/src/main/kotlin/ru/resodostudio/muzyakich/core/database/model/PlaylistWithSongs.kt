package ru.resodostudio.muzyakich.core.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithSongs(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "uuid",
        associateBy = Junction(
            PlaylistSongCrossRef::class,
            parentColumn = "playlist_uuid",
            entityColumn = "song_uuid",
        ),
    )
    val songs: List<SongEntity>,
)