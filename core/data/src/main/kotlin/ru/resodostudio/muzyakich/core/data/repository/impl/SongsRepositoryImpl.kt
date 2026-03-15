package ru.resodostudio.muzyakich.core.data.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.database.dao.SongDao
import ru.resodostudio.muzyakich.core.database.model.SongEntity
import ru.resodostudio.muzyakich.core.mediastore.MediaStoreDataSource
import ru.resodostudio.muzyakich.core.mediastore.model.asExternalModel
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.uuid.Uuid

internal class SongsRepositoryImpl @Inject constructor(
    private val mediaStoreDataSource: MediaStoreDataSource,
    private val songDao: SongDao,
) : SongsRepository {

    override fun getSong(mediaId: String): Flow<Song?> {
        return combine(
            mediaStoreDataSource.songs,
            songDao.getSong(mediaId),
        ) { mediaStoreSongs, songEntity ->
            mediaStoreSongs
                .find { it.mediaId == mediaId }
                ?.asExternalModel(
                    isFavorite = songEntity?.isFavorite ?: false,
                    playCount = songEntity?.playCount ?: 0,
                )
        }
    }

    override fun getSongs(
        sortBy: SortBy,
        sortOrder: SortOrder,
    ): Flow<List<Song>> {
        return combine(
            mediaStoreDataSource.songs,
            songDao.getSongs(),
        ) { mediaStoreSongs, songEntities ->
            mediaStoreSongs.mapTo(ArrayList(mediaStoreSongs.size)) { song ->
                val songEntity = songEntities.find { it.mediaId == song.mediaId }
                song.asExternalModel(
                    isFavorite = songEntity?.isFavorite ?: false,
                    playCount = songEntity?.playCount ?: 0,
                )
            }.apply {
                val comparator = when (sortBy) {
                    SortBy.ARTIST -> compareBy(Song::artist)
                    SortBy.TITLE -> compareBy(Song::title)
                }
                sortWith(if (sortOrder == SortOrder.ASCENDING) comparator else comparator.reversed())
            }
        }
    }

    override suspend fun toggleFavorite(mediaId: String, isFavorite: Boolean) {
        val songEntity = songDao.getSong(mediaId).first()
        songDao.upsertSong(
            SongEntity(
                uuid = songEntity?.uuid ?: Uuid.random(),
                mediaId = mediaId,
                isFavorite = isFavorite,
                playCount = songEntity?.playCount ?: 0,
                lastPlayed = songEntity?.lastPlayed,
            )
        )
    }

    override suspend fun incrementPlayCount(mediaId: String) {
        val lastPlayed = Clock.System.now()
        val updatedRows = songDao.incrementPlayCount(mediaId, lastPlayed)
        if (updatedRows == 0) {
            songDao.upsertSong(
                SongEntity(
                    uuid = Uuid.random(),
                    mediaId = mediaId,
                    isFavorite = false,
                    playCount = 1,
                    lastPlayed = lastPlayed,
                )
            )
        }
    }
}