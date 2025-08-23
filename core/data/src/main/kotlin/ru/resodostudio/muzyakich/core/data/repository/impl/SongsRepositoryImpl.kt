package ru.resodostudio.muzyakich.core.data.repository.impl

import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.mediastore.MediaStoreDataSource
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import javax.inject.Inject

internal class SongsRepositoryImpl @Inject constructor(
    private val mediaStoreDataSource: MediaStoreDataSource,
) : SongsRepository {

    override fun getSongs(
        sortBy: SortBy,
        sortOrder: SortOrder,
    ): Flow<List<Song>> = mediaStoreDataSource.getSongs(sortBy, sortOrder)
}