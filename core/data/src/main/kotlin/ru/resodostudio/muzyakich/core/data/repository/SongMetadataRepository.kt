package ru.resodostudio.muzyakich.core.data.repository

import ru.resodostudio.muzyakich.core.model.SongMetadata

interface SongMetadataRepository {

    suspend fun getSongMetadata(filePath: String): SongMetadata?

    suspend fun updateSongMetadata(
        filePath: String,
        mediaUri: String,
        songMetadata: SongMetadata,
        isArtworkChanged: Boolean,
    ): Result<Unit>
}
