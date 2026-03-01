package ru.resodostudio.muzyakich.core.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.Default
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.model.data.Artist
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import javax.inject.Inject

class GetArtistsUseCase @Inject constructor(
    @Dispatcher(Default) private val defaultDispatcher: CoroutineDispatcher,
    private val songsRepository: SongsRepository,
) {
    operator fun invoke(
        sortBy: SortBy = SortBy.ARTIST,
        sortOrder: SortOrder = SortOrder.ASCENDING,
    ): Flow<List<Artist>> {
        return songsRepository.getSongs(sortBy, sortOrder)
            .map { songs ->
                songs.groupBy(Song::artistId).map { (artistId, artistSongs) ->
                    Artist(
                        id = artistId,
                        name = artistSongs.firstOrNull()?.artist ?: "<unknown>",
                        songs = artistSongs,
                    )
                }
            }
            .flowOn(defaultDispatcher)
    }
}