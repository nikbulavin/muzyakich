package ru.resodostudio.muzyakich.core.database.model

import androidx.room3.Embedded
import androidx.room3.Junction
import androidx.room3.Relation

data class PlaylistWithSongs(
    @Embedded
    val playlist: PlaylistEntity,
    @Relation(
        parentColumns = ["uuid"],
        entityColumns = ["uuid"],
        associateBy = Junction(
            PlaylistSongCrossRef::class,
            parentColumns = ["playlist_uuid"],
            entityColumns = ["song_uuid"],
        ),
    )
    val songs: List<SongEntity>,
)