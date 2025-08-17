package ru.resodostudio.muzyakich.core.data.repository.impl

import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.data.repository.MediaRepository
import ru.resodostudio.muzyakich.core.mediastore.MediaStoreDataSource
import ru.resodostudio.muzyakich.core.model.data.Song
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    mediaStoreDataSource: MediaStoreDataSource,
) : MediaRepository {

    override val songs: Flow<List<Song>> = mediaStoreDataSource.getSongs()
}