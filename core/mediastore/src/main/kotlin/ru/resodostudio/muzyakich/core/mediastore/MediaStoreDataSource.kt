package ru.resodostudio.muzyakich.core.mediastore

import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder

interface MediaStoreDataSource {

    fun getSongs(
        sortBy: SortBy,
        sortOrder: SortOrder,
    ): Flow<List<Song>>
}