package ru.resodostudio.muzyakich.core.mediastore

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import ru.resodostudio.muzyakich.core.mediastore.util.MediaStoreConfig
import ru.resodostudio.muzyakich.core.mediastore.util.asArtworkUri
import ru.resodostudio.muzyakich.core.mediastore.util.asFolder
import ru.resodostudio.muzyakich.core.mediastore.util.getInt
import ru.resodostudio.muzyakich.core.mediastore.util.getLong
import ru.resodostudio.muzyakich.core.mediastore.util.getString
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
                        MediaStoreConfig.Song.Projection,
                        "${MediaStore.Audio.Media.IS_MUSIC} = ?",
                        arrayOf("1"),
                        "${MediaStore.Audio.Media.ARTIST} ASC",
                    )?.use { cursor ->
                        while (cursor.moveToNext()) {
                            val id = cursor.getLong(MediaStore.Audio.Media._ID)
                            val artistId = cursor.getLong(MediaStore.Audio.Media.ARTIST_ID)
                            val albumId = cursor.getLong(MediaStore.Audio.Media.ALBUM_ID)
                            val title = cursor.getString(MediaStore.Audio.Media.TITLE)
                            val artist = cursor.getString(MediaStore.Audio.Media.ARTIST)
                            val album = cursor.getString(MediaStore.Audio.Media.ALBUM)
                            val duration = cursor.getLong(MediaStore.Audio.Media.DURATION)
                            val bitrate = cursor.getInt(MediaStore.Audio.Media.BITRATE)
                            val isFavorite = cursor.getInt(MediaStore.Audio.Media.IS_FAVORITE)

                            val mediaId = id.toString()
                            val mediaUri = ContentUris.withAppendedId(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                id,
                            )

                            val folder = cursor.getString(MediaStore.Audio.Media.DATA).asFolder()

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
                                    bitrate = bitrate,
                                    isFavorite = isFavorite == 1,
                                )
                            )
                        }
                    }
                }
            }
}