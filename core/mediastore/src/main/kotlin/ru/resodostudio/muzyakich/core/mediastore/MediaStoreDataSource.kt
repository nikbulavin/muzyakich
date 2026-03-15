package ru.resodostudio.muzyakich.core.mediastore

import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.mediastore.model.MediaStoreSong

interface MediaStoreDataSource {

    val songs: Flow<List<MediaStoreSong>>
}