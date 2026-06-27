package ru.resodostudio.muzyakich.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.model.Playlist
import kotlin.uuid.Uuid

interface PlaylistsRepository {

    fun getPlaylist(uuid: Uuid): Flow<Playlist?>

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun upsertPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(uuid: Uuid)

    suspend fun addSongToPlaylist(playlistUuid: Uuid, songMediaId: String, position: Int)

    suspend fun removeSongFromPlaylist(playlistUuid: Uuid, songMediaId: String)
}