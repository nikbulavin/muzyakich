package ru.resodostudio.muzyakich.core.data.model

import ru.resodostudio.muzyakich.core.database.model.PlaylistEntity
import ru.resodostudio.muzyakich.core.model.Playlist
import ru.resodostudio.muzyakich.core.model.PlaylistSong

fun PlaylistEntity.asExternalModel(songs: List<PlaylistSong> = emptyList()): Playlist {
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
