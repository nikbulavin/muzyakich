package ru.resodostudio.muzyakich.core.mediastore

import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.model.data.Song

interface MediaStoreDataSource {

    val songs: Flow<List<Song>>
}