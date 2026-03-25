package ru.resodostudio.muzyakich.core.data.model

import ru.resodostudio.muzyakich.core.database.model.PlaylistEntity
import ru.resodostudio.muzyakich.core.model.data.Playlist
import ru.resodostudio.muzyakich.core.model.data.Song

fun PlaylistEntity.asExternalModel(songs: List<Song> = emptyList()): Playlist {
    return Playlist(
        uuid = uuid,
        title = title,
        timestamp = timestamp,
        coverFilePath = coverFilePath,
        songs = songs,
    )
}

fun Playlist.asEntity(): PlaylistEntity {
    return PlaylistEntity(
        uuid = uuid,
        title = title,
        timestamp = timestamp,
        coverFilePath = coverFilePath,
    )
}