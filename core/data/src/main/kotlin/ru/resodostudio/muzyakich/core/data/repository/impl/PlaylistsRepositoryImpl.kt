package ru.resodostudio.muzyakich.core.data.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import ru.resodostudio.muzyakich.core.data.model.asEntity
import ru.resodostudio.muzyakich.core.data.model.asExternalModel
import ru.resodostudio.muzyakich.core.data.repository.PlaylistsRepository
import ru.resodostudio.muzyakich.core.database.dao.PlaylistDao
import ru.resodostudio.muzyakich.core.database.dao.SongDao
import ru.resodostudio.muzyakich.core.database.model.PlaylistSongCrossRef
import ru.resodostudio.muzyakich.core.database.model.SongEntity
import ru.resodostudio.muzyakich.core.mediastore.MediaStoreDataSource
import ru.resodostudio.muzyakich.core.mediastore.model.asExternalModel
import ru.resodostudio.muzyakich.core.model.data.Playlist
import javax.inject.Inject
import kotlin.uuid.Uuid

internal class PlaylistsRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao,
    private val songDao: SongDao,
    private val mediaStoreDataSource: MediaStoreDataSource,
) : PlaylistsRepository {

    override fun getPlaylist(uuid: Uuid): Flow<Playlist?> {
        return combine(
            playlistDao.getPlaylistWithSongsEntity(uuid),
            playlistDao.getPlaylistSongCrossRefs(uuid),
            mediaStoreDataSource.songs,
        ) { playlistWithSongs, crossRefs, mediaStoreSongs ->
            playlistWithSongs?.let {
                val orderedSongs = crossRefs.mapNotNull { crossRef ->
                    val songEntity = it.songs.find { entity -> entity.uuid == crossRef.songUuid }
                        ?: return@mapNotNull null
                    mediaStoreSongs.find { mediaStoreSong -> mediaStoreSong.mediaId == songEntity.mediaId }?.asExternalModel(
                        isFavorite = songEntity.isFavorite,
                        playCount = songEntity.playCount,
                    )
                }
                it.playlist.asExternalModel(orderedSongs)
            }
        }
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return combine(
            playlistDao.getPlaylistWithSongsEntities(),
            playlistDao.getAllPlaylistSongCrossRefs(),
            mediaStoreDataSource.songs,
        ) { playlistsWithSongs, allCrossRefs, mediaStoreSongs ->
            playlistsWithSongs.map { playlistWithSongs ->
                val playlistCrossRefs = allCrossRefs.filter { it.playlistUuid == playlistWithSongs.playlist.uuid }
                val orderedSongs = playlistCrossRefs.mapNotNull { crossRef ->
                    val songEntity = playlistWithSongs.songs.find { entity -> entity.uuid == crossRef.songUuid }
                        ?: return@mapNotNull null
                    mediaStoreSongs.find { mediaStoreSong -> mediaStoreSong.mediaId == songEntity.mediaId }?.asExternalModel(
                        isFavorite = songEntity.isFavorite,
                        playCount = songEntity.playCount,
                    )
                }
                playlistWithSongs.playlist.asExternalModel(orderedSongs)
            }
        }
    }

    override suspend fun upsertPlaylist(playlist: Playlist) {
        val crossRefs = playlist.songs.mapIndexed { index, song ->
            val songUuid = ensureSongEntityExists(song.mediaId)
            PlaylistSongCrossRef(
                playlistUuid = playlist.uuid,
                songUuid = songUuid,
                position = index,
            )
        }
        playlistDao.updatePlaylistWithSongs(playlist.asEntity(), crossRefs)
    }

    override suspend fun deletePlaylist(uuid: Uuid) {
        playlistDao.deletePlaylist(uuid)
    }

    override suspend fun addSongToPlaylist(playlistUuid: Uuid, songMediaId: String, position: Int) {
        val songUuid = ensureSongEntityExists(songMediaId)

        playlistDao.upsertPlaylistSongCrossRefs(
            listOf(
                PlaylistSongCrossRef(
                    playlistUuid = playlistUuid,
                    songUuid = songUuid,
                    position = position,
                )
            )
        )
    }

    private suspend fun ensureSongEntityExists(songMediaId: String): Uuid {
        val songEntity = songDao.getSong(songMediaId).first()
        val songUuid = songEntity?.uuid ?: Uuid.random()
        
        if (songEntity == null) {
            songDao.upsertSong(
                SongEntity(
                    uuid = songUuid,
                    mediaId = songMediaId,
                    isFavorite = false,
                    playCount = 0,
                    lastPlayed = null,
                )
            )
        }
        return songUuid
    }
}