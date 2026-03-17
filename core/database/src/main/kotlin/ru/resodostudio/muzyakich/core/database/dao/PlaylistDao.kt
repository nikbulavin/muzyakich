package ru.resodostudio.muzyakich.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.database.model.PlaylistEntity
import ru.resodostudio.muzyakich.core.database.model.PlaylistSongCrossRef
import ru.resodostudio.muzyakich.core.database.model.PlaylistWithSongs
import kotlin.uuid.Uuid

@Dao
interface PlaylistDao {

    @Transaction
    @Query("SELECT * FROM playlists WHERE uuid = :uuid")
    fun getPlaylistWithSongsEntity(uuid: Uuid): Flow<PlaylistWithSongs?>

    @Transaction
    @Query("SELECT * FROM playlists ORDER BY timestamp DESC")
    fun getPlaylistWithSongsEntities(): Flow<List<PlaylistWithSongs>>

    @Upsert
    suspend fun upsertPlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM playlists WHERE uuid = :uuid")
    suspend fun deletePlaylist(uuid: Uuid)

    @Upsert
    suspend fun upsertPlaylistSongCrossRef(crossRef: PlaylistSongCrossRef)

    @Delete
    suspend fun deletePlaylistSongCrossRef(crossRef: PlaylistSongCrossRef)
}