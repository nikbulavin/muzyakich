package ru.resodostudio.muzyakich.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.database.model.FavoriteSongEntity

@Dao
interface FavoriteSongDao {

    @Query("SELECT media_id FROM favorite_songs")
    fun getFavoriteMediaIds(): Flow<List<Long>>

    @Upsert
    suspend fun upsertFavoriteSong(favoriteSongEntity: FavoriteSongEntity)

    @Query("DELETE FROM favorite_songs WHERE media_id = :mediaId")
    suspend fun deleteFavoriteSong(mediaId: Long)
}