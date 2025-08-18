package ru.resodostudio.muzyakich.core.mediastore

import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.model.data.Song

interface MediaStoreDataSource {

    fun getSongs(): Flow<List<Song>>
}