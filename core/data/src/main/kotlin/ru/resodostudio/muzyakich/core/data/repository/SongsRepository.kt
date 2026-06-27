package ru.resodostudio.muzyakich.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.model.Song
import ru.resodostudio.muzyakich.core.model.SortBy
import ru.resodostudio.muzyakich.core.model.SortOrder

interface SongsRepository {

    fun getSong(mediaId: String): Flow<Song?>

    fun getSongs(
        sortBy: SortBy,
        sortOrder: SortOrder,
    ): Flow<List<Song>>

    suspend fun toggleFavorite(mediaId: String, isFavorite: Boolean)

    suspend fun incrementPlayCount(mediaId: String)
}