package ru.resodostudio.muzyakich.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.model.data.Song

interface SongsRepository {

    fun getSongs(): Flow<List<Song>>
}