package ru.resodostudio.muzyakich.core.data.repository.impl

import android.content.Context
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.IO
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
import java.io.File
import javax.inject.Inject
import kotlin.uuid.Uuid

internal class PlaylistsRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao,
    private val songDao: SongDao,
    private val mediaStoreDataSource: MediaStoreDataSource,
    @ApplicationContext private val context: Context,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : PlaylistsRepository {

    override fun getPlaylist(uuid: Uuid): Flow<Playlist?> {
        return combine(
            playlistDao.getPlaylistWithSongsEntity(uuid),
            playlistDao.getPlaylistSongCrossRefs(uuid),
            mediaStoreDataSource.songs,
        ) { playlistWithSongs, crossRefs, mediaStoreSongs ->
            playlistWithSongs?.let {
                val orderedSongs = crossRefs.mapNotNull { crossRef ->
                    val songEntity = it.songs
                        .find { entity -> entity.uuid == crossRef.songUuid }
                        ?: return@mapNotNull null
                    mediaStoreSongs.find { mediaStoreSong -> mediaStoreSong.mediaId == songEntity.mediaId }
                        ?.asExternalModel(
                            isFavorite = songEntity.isFavorite,
                            playCount = songEntity.playCount,
                        )
                }

                it.playlist.asExternalModel(songs = orderedSongs)
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
                val playlistCrossRefs = allCrossRefs
                    .filter { it.playlistUuid == playlistWithSongs.playlist.uuid }
                val orderedSongs = playlistCrossRefs.mapNotNull { crossRef ->
                    val songEntity = playlistWithSongs.songs
                        .find { entity -> entity.uuid == crossRef.songUuid }
                        ?: return@mapNotNull null
                    mediaStoreSongs.find { mediaStoreSong -> mediaStoreSong.mediaId == songEntity.mediaId }
                        ?.asExternalModel(
                            isFavorite = songEntity.isFavorite,
                            playCount = songEntity.playCount,
                        )
                }

                playlistWithSongs.playlist.asExternalModel(songs = orderedSongs)
            }
        }
    }

    override suspend fun upsertPlaylist(playlist: Playlist) {
        val oldCoverPath = playlistDao.getPlaylistWithSongsEntity(playlist.uuid)
            .first()?.playlist?.coverFilePath

        val finalCoverPath = if (playlist.coverFilePath != oldCoverPath) {
            oldCoverPath?.let { deleteCoverFile(it) }
            playlist.coverFilePath?.let { coverFilePath ->
                saveCoverFile(
                    uriString = coverFilePath,
                    playlistUuid = playlist.uuid,
                )
            }
        } else {
            oldCoverPath
        }

        val finalPlaylist = playlist.copy(coverFilePath = finalCoverPath)

        val crossRefs = finalPlaylist.songs.mapIndexed { index, song ->
            PlaylistSongCrossRef(
                playlistUuid = finalPlaylist.uuid,
                songUuid = ensureSongEntityExists(song.mediaId),
                position = index,
            )
        }
        playlistDao.updatePlaylistWithSongs(finalPlaylist.asEntity(), crossRefs)
    }

    override suspend fun deletePlaylist(uuid: Uuid) {
        playlistDao.getPlaylistWithSongsEntity(uuid)
            .first()?.playlist?.coverFilePath
            ?.let { deleteCoverFile(it) }
        playlistDao.deletePlaylist(uuid)
    }

    override suspend fun addSongToPlaylist(playlistUuid: Uuid, songMediaId: String, position: Int) {
        playlistDao.upsertPlaylistSongCrossRefs(
            listOf(
                PlaylistSongCrossRef(
                    playlistUuid = playlistUuid,
                    songUuid = ensureSongEntityExists(songMediaId),
                    position = position,
                )
            )
        )
    }

    override suspend fun removeSongFromPlaylist(playlistUuid: Uuid, songMediaId: String) {
        val songUuid = songDao.getSong(songMediaId).first()?.uuid
        if (songUuid != null) {
            playlistDao.deletePlaylistSongCrossRef(playlistUuid, songUuid)
        }
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

    private suspend fun getCoversDir(): File = withContext(ioDispatcher) {
        File(context.filesDir, "playlist_covers").apply {
            if (!exists()) mkdirs()
        }
    }

    private suspend fun saveCoverFile(uriString: String, playlistUuid: Uuid): String? =
        withContext(ioDispatcher) {
            runCatching {
                val destinationFile = File(getCoversDir(), "$playlistUuid.jpg")
                context.contentResolver.openInputStream(uriString.toUri())?.use { input ->
                    destinationFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                destinationFile.absolutePath
            }.getOrNull()
        }

    private suspend fun deleteCoverFile(filePath: String) {
        withContext(ioDispatcher) {
            runCatching {
                File(filePath).takeIf { it.exists() }?.delete()
            }
        }
    }
}