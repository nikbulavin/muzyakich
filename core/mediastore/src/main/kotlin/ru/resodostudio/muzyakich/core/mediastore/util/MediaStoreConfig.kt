package ru.resodostudio.muzyakich.core.mediastore.util

import android.net.Uri
import android.provider.MediaStore

internal object MediaStoreConfig {

    object Song {

        val Collection: Uri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

        val Projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.BITRATE,
            MediaStore.Audio.Media.IS_FAVORITE,
            MediaStore.Audio.Media.SIZE,
        )
    }
}
