package ru.resodostudio.muzyakich.core.data.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.database.dao.FavoriteSongDao
import ru.resodostudio.muzyakich.core.database.model.FavoriteSongEntity
import ru.resodostudio.muzyakich.core.mediastore.MediaStoreDataSource
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import javax.inject.Inject

internal class SongsRepositoryImpl @Inject constructor(
    private val mediaStoreDataSource: MediaStoreDataSource,
    private val favoriteSongDao: FavoriteSongDao,
) : SongsRepository {

    override fun getSong(mediaId: String): Flow<Song?> {
        return combine(
            mediaStoreDataSource.songs,
            favoriteSongDao.getFavoriteMediaIds(),
        ) { songs, favoriteMediaIds ->
            val song = songs.find { it.mediaId == mediaId }
            song?.copy(isFavorite = song.mediaId in favoriteMediaIds.toSet())
        }
    }

    override fun getSongs(
        sortBy: SortBy,
        sortOrder: SortOrder,
    ): Flow<List<Song>> {
        return combine(
            mediaStoreDataSource.songs,
            favoriteSongDao.getFavoriteMediaIds(),
        ) { songs, favoriteMediaIds ->
            songs.mapTo(ArrayList(songs.size)) { song ->
                song.copy(isFavorite = song.mediaId in favoriteMediaIds.toSet())
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
        if (isFavorite) {
            favoriteSongDao.upsertFavoriteSong(FavoriteSongEntity(mediaId))
        } else {
            favoriteSongDao.deleteFavoriteSong(mediaId)
        }
    }
}
