package ru.resodostudio.muzyakich.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.mediastore.Song

interface MediaRepository {

    val songs: Flow<List<Song>>
}