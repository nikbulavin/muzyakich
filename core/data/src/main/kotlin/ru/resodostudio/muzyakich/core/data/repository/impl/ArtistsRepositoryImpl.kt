package ru.resodostudio.muzyakich.core.data.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.resodostudio.muzyakich.core.data.repository.ArtistsRepository
import ru.resodostudio.muzyakich.core.mediastore.MediaStoreDataSource
import ru.resodostudio.muzyakich.core.model.data.Artist
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import javax.inject.Inject

internal class ArtistsRepositoryImpl @Inject constructor(
    private val mediaStoreDataSource: MediaStoreDataSource,
) : ArtistsRepository {

    override fun getArtists(): Flow<List<Artist>> {
        return mediaStoreDataSource.getSongs(sortBy = SortBy.TITLE, sortOrder = SortOrder.ASCENDING)
            .map { songs ->
                songs.groupBy(Song::artistId).map { (artistId, songs) ->
                    Artist(
                        id = artistId,
                        name = songs.firstOrNull()?.artist ?: "<unknown>",
                        songs = songs,
                    )
                }
            }
    }
}