package ru.resodostudio.muzyakich.core.mediastore

import android.content.ContentUris
import android.content.Context
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.IO
import ru.resodostudio.muzyakich.core.mediastore.util.MediaStoreConfig
import ru.resodostudio.muzyakich.core.mediastore.util.asArtworkUri
import ru.resodostudio.muzyakich.core.mediastore.util.buildMediaStoreSortOrder
import ru.resodostudio.muzyakich.core.mediastore.util.getAlbum
import ru.resodostudio.muzyakich.core.mediastore.util.getAlbumId
import ru.resodostudio.muzyakich.core.mediastore.util.getArtist
import ru.resodostudio.muzyakich.core.mediastore.util.getArtistId
import ru.resodostudio.muzyakich.core.mediastore.util.getBitrate
import ru.resodostudio.muzyakich.core.mediastore.util.getBitsPerSample
import ru.resodostudio.muzyakich.core.mediastore.util.getDataFolder
import ru.resodostudio.muzyakich.core.mediastore.util.getDuration
import ru.resodostudio.muzyakich.core.mediastore.util.getIsFavorite
import ru.resodostudio.muzyakich.core.mediastore.util.getMediaId
import ru.resodostudio.muzyakich.core.mediastore.util.getSampleRate
import ru.resodostudio.muzyakich.core.mediastore.util.getSize
import ru.resodostudio.muzyakich.core.mediastore.util.getTitle
import ru.resodostudio.muzyakich.core.mediastore.util.getTrackNumber
import ru.resodostudio.muzyakich.core.mediastore.util.observe
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import javax.inject.Inject
import kotlin.uuid.Uuid

internal class MediaStoreDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : MediaStoreDataSource {

    override fun getSongs(
        sortBy: SortBy,
        sortOrder: SortOrder,
    ): Flow<List<Song>> {
        return context.contentResolver
            .observe(uri = MediaStoreConfig.Song.Collection)
            .map {
                val songs = buildList {
                    context.contentResolver.query(
                        MediaStoreConfig.Song.Collection,
                        MediaStoreConfig.Song.Projection.toTypedArray(),
                        "${MediaStore.Audio.Media.IS_MUSIC} = ?",
                        arrayOf("1"),
                        buildMediaStoreSortOrder(sortBy, sortOrder),
                    )?.use { cursor ->
                        while (cursor.moveToNext()) {
                            val id = cursor.getMediaId()
                            val albumId = cursor.getAlbumId()

                            val mediaUri = ContentUris.withAppendedId(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                id,
                            )

                            add(
                                Song(
                                    uuid = Uuid.random(),
                                    mediaId = cursor.getMediaId().toString(),
                                    artistId = cursor.getArtistId(),
                                    albumId = albumId,
                                    mediaUri = mediaUri,
                                    artworkUri = albumId.asArtworkUri(),
                                    title = cursor.getTitle(),
                                    artist = cursor.getArtist(),
                                    album = cursor.getAlbum(),
                                    folder = cursor.getDataFolder(),
                                    duration = cursor.getDuration(),
                                    bitrate = cursor.getBitrate() / 1000,
                                    isFavorite = cursor.getIsFavorite() == 1,
                                    size = cursor.getSize(),
                                    bitsPerSample = cursor.getBitsPerSample(),
                                    sampleRate = cursor.getSampleRate(),
                                    trackNumber = cursor.getTrackNumber(),
                                    year = 0,
                                )
                            )
                        }
                    }
                }

                coroutineScope {
                    songs.map { song ->
                        async {
                            var year = song.year
                            MediaMetadataRetriever().apply {
                                runCatching {
                                    setDataSource(context, song.mediaUri)
                                    extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)
                                        ?.takeIf { it.length >= 4 }
                                        ?.take(4)
                                        ?.toIntOrNull()
                                        ?.let { year = it }
                                }.onFailure { it.printStackTrace() }
                                runCatching { release() }
                            }
                            song.copy(year = year)
                        }
                    }.awaitAll()
                }
            }
            .flowOn(ioDispatcher)
    }
}
