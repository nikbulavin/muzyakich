package ru.resodostudio.muzyakich.core.mediastore

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import ru.resodostudio.muzyakich.core.mediastore.util.MediaStoreConfig
import ru.resodostudio.muzyakich.core.mediastore.util.asArtworkUri
import ru.resodostudio.muzyakich.core.mediastore.util.getAlbum
import ru.resodostudio.muzyakich.core.mediastore.util.getAlbumId
import ru.resodostudio.muzyakich.core.mediastore.util.getArtist
import ru.resodostudio.muzyakich.core.mediastore.util.getArtistId
import ru.resodostudio.muzyakich.core.mediastore.util.getBitrate
import ru.resodostudio.muzyakich.core.mediastore.util.getBitsPerSample
import ru.resodostudio.muzyakich.core.mediastore.util.getDuration
import ru.resodostudio.muzyakich.core.mediastore.util.getDataFolder
import ru.resodostudio.muzyakich.core.mediastore.util.getIsFavorite
import ru.resodostudio.muzyakich.core.mediastore.util.getMediaId
import ru.resodostudio.muzyakich.core.mediastore.util.getSampleRate
import ru.resodostudio.muzyakich.core.mediastore.util.getSize
import ru.resodostudio.muzyakich.core.mediastore.util.getTitle
import ru.resodostudio.muzyakich.core.mediastore.util.observe
import ru.resodostudio.muzyakich.core.model.data.Song
import javax.inject.Inject

class MediaStoreDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun getSongs() =
        context.contentResolver
            .observe(uri = MediaStoreConfig.Song.Collection)
            .map {
                buildList {
                    context.contentResolver.query(
                        MediaStoreConfig.Song.Collection,
                        MediaStoreConfig.Song.Projection.toTypedArray(),
                        "${MediaStore.Audio.Media.IS_MUSIC} = ?",
                        arrayOf("1"),
                        "${MediaStore.Audio.Media.ARTIST} ASC",
                    )?.use { cursor ->
                        while (cursor.moveToNext()) {
                            val id = cursor.getMediaId()
                            val artistId = cursor.getArtistId()
                            val albumId = cursor.getAlbumId()
                            val title = cursor.getTitle()
                            val artist = cursor.getArtist()
                            val album = cursor.getAlbum()
                            val duration = cursor.getDuration()
                            val bitrate = cursor.getBitrate()
                            val isFavorite = cursor.getIsFavorite()
                            val size = cursor.getSize()
                            val bitsPerSample = cursor.getBitsPerSample()
                            val sampleRate = cursor.getSampleRate()

                            val mediaId = id.toString()
                            val mediaUri = ContentUris.withAppendedId(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                id,
                            )

                            val folder = cursor.getDataFolder()

                            add(
                                Song(
                                    mediaId = mediaId,
                                    artistId = artistId,
                                    albumId = albumId,
                                    mediaUri = mediaUri,
                                    artworkUri = albumId.asArtworkUri(),
                                    title = title,
                                    artist = artist,
                                    album = album,
                                    folder = folder,
                                    duration = duration,
                                    bitrate = bitrate / 1000,
                                    isFavorite = isFavorite == 1,
                                    size = size,
                                    bitsPerSample = bitsPerSample,
                                    sampleRate = sampleRate,
                                )
                            )
                        }
                    }
                }
            }
}