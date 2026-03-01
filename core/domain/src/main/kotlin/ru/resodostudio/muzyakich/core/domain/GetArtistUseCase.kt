package ru.resodostudio.muzyakich.core.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.Default
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.model.data.Artist
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import javax.inject.Inject

class GetArtistUseCase @Inject constructor(
    @Dispatcher(Default) private val defaultDispatcher: CoroutineDispatcher,
    private val songsRepository: SongsRepository,
) {
    operator fun invoke(artistId: Long): Flow<Artist?> {
        return songsRepository.getSongs(SortBy.ARTIST, SortOrder.ASCENDING)
            .map { songs ->
                val artistSongs = songs.filter { it.artistId == artistId }

                if (artistSongs.isEmpty()) {
                    null
                } else {
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