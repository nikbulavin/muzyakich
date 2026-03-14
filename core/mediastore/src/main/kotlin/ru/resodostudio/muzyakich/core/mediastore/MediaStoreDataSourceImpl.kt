package ru.resodostudio.muzyakich.core.mediastore

import android.content.ContentUris
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.IO
import ru.resodostudio.muzyakich.core.common.di.ApplicationScope
import ru.resodostudio.muzyakich.core.mediastore.util.MediaStoreConfig
import ru.resodostudio.muzyakich.core.mediastore.util.asArtworkUri
import ru.resodostudio.muzyakich.core.mediastore.util.getAlbum
import ru.resodostudio.muzyakich.core.mediastore.util.getAlbumId
import ru.resodostudio.muzyakich.core.mediastore.util.getArtist
import ru.resodostudio.muzyakich.core.mediastore.util.getArtistId
import ru.resodostudio.muzyakich.core.mediastore.util.getBitrate
import ru.resodostudio.muzyakich.core.mediastore.util.getBitsPerSample
import ru.resodostudio.muzyakich.core.mediastore.util.getDuration
import ru.resodostudio.muzyakich.core.mediastore.util.getGenre
import ru.resodostudio.muzyakich.core.mediastore.util.getMediaId
import ru.resodostudio.muzyakich.core.mediastore.util.getPath
import ru.resodostudio.muzyakich.core.mediastore.util.getSampleRate
import ru.resodostudio.muzyakich.core.mediastore.util.getSize
import ru.resodostudio.muzyakich.core.mediastore.util.getTitle
import ru.resodostudio.muzyakich.core.mediastore.util.getTrackNumber
import ru.resodostudio.muzyakich.core.mediastore.util.getYear
import ru.resodostudio.muzyakich.core.mediastore.util.observe
import ru.resodostudio.muzyakich.core.model.data.Song
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid

@Singleton
internal class MediaStoreDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope private val applicationScope: CoroutineScope,
) : MediaStoreDataSource {

    private val isMusicSelectionArgs = arrayOf("1")
    private val retrieverSemaphore = Semaphore(10)

    override val songs: Flow<List<Song>> = context.contentResolver
        .observe(uri = MediaStoreConfig.Song.Collection)
        .map {
            val songs = buildList {
                context.contentResolver.query(
                    MediaStoreConfig.Song.Collection,
                    MediaStoreConfig.Song.Projection,
                    "${MediaStore.Audio.Media.IS_MUSIC} = ?",
                    isMusicSelectionArgs,
                    "${MediaStore.Audio.Media.TITLE} ASC",
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
                                path = cursor.getPath(),
                                duration = cursor.getDuration(),
                                bitrate = cursor.getBitrate() / 1000,
                                isFavorite = false,
                                size = cursor.getSize(),
                                bitsPerSample = cursor.getBitsPerSample(),
                                sampleRate = cursor.getSampleRate(),
                                trackNumber = cursor.getTrackNumber(),
                                year = cursor.getYear(),
                                genre = cursor.getGenre(),
                            )
                        )
                    }
                }
            }

            coroutineScope {
                val songsToUpdateDeferred = songs
                    .filter { it.year == 0 }
                    .map { song ->
                        async {
                            val extractedYear = extractYear(context, song.mediaUri)
                            song.copy(year = extractedYear)
                        }
                    }

                val updatedSongsMap = songsToUpdateDeferred.awaitAll().associateBy { it.mediaId }

                songs.map { song ->
                    updatedSongsMap[song.mediaId] ?: song
                }
            }
        }
        .catch { exception ->
            exception.printStackTrace()
            emit(emptyList())
        }
        .flowOn(ioDispatcher)
        .shareIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            replay = 1,
        )

    private suspend fun extractYear(context: Context, uri: Uri): Int {
        return retrieverSemaphore.withPermit {
            MediaMetadataRetriever().use { retriever ->
                runCatching {
                    retriever.setDataSource(context, uri)
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)
                        ?.takeIf { it.length >= 4 }
                        ?.take(4)
                        ?.toIntOrNull()
                }.getOrNull() ?: 0
            }
        }
    }
}
