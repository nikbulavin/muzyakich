package ru.resodostudio.muzyakich.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.model.data.Artist

interface ArtistsRepository {

    fun getArtists(): Flow<List<Artist>>
}