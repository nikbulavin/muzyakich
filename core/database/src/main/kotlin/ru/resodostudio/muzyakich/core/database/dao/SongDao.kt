package ru.resodostudio.muzyakich.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.database.model.SongEntity
import kotlin.time.Instant

@Dao
interface SongDao {

    @Query("SELECT * FROM songs")
    fun getSongs(): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE media_id = :mediaId")
    fun getSong(mediaId: String): Flow<SongEntity?>

    @Upsert
    suspend fun upsertSong(songEntity: SongEntity)

    @Query("UPDATE songs SET play_count = play_count + 1, last_played = :lastPlayed WHERE media_id = :mediaId")
    suspend fun incrementPlayCount(mediaId: String, lastPlayed: Instant?): Int

    @Query("DELETE FROM songs WHERE media_id = :mediaId")
    suspend fun deleteSong(mediaId: String)
}