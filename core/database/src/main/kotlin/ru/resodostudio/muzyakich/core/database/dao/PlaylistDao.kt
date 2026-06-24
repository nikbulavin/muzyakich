package ru.resodostudio.muzyakich.core.database.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
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

    @Query("SELECT * FROM playlist_songs WHERE playlist_uuid = :playlistUuid ORDER BY position ASC")
    fun getPlaylistSongCrossRefs(playlistUuid: Uuid): Flow<List<PlaylistSongCrossRef>>

    @Query("SELECT * FROM playlist_songs ORDER BY playlist_uuid, position ASC")
    fun getAllPlaylistSongCrossRefs(): Flow<List<PlaylistSongCrossRef>>

    @Upsert
    suspend fun upsertPlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM playlists WHERE uuid = :uuid")
    suspend fun deletePlaylist(uuid: Uuid)

    @Query("DELETE FROM playlist_songs WHERE playlist_uuid = :playlistUuid")
    suspend fun clearPlaylistSongs(playlistUuid: Uuid)

    @Query("DELETE FROM playlist_songs WHERE playlist_uuid = :playlistUuid AND song_uuid = :songUuid")
    suspend fun deletePlaylistSongCrossRef(playlistUuid: Uuid, songUuid: Uuid)

    @Upsert
    suspend fun upsertPlaylistSongCrossRefs(crossRefs: List<PlaylistSongCrossRef>)

    @Transaction
    suspend fun updatePlaylistWithSongs(
        playlist: PlaylistEntity,
        crossRefs: List<PlaylistSongCrossRef>,
    ) {
        upsertPlaylist(playlist)
        clearPlaylistSongs(playlist.uuid)
        upsertPlaylistSongCrossRefs(crossRefs)
    }
}