package ru.resodostudio.muzyakich.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.database.model.SongEntity

@Dao
interface SongDao {

    @Query("SELECT * FROM songs")
    fun getSongs(): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE media_id = :mediaId")
    fun getSong(mediaId: String): Flow<SongEntity?>

    @Upsert
    suspend fun upsertSong(songEntity: SongEntity)

    @Query("DELETE FROM songs WHERE media_id = :mediaId")
    suspend fun deleteSong(mediaId: String)
}