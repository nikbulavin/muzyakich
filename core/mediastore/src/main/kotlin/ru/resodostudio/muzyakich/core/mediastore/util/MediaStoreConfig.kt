package ru.resodostudio.muzyakich.core.mediastore.util

import android.net.Uri
import android.os.Build
import android.os.ext.SdkExtensions
import android.provider.MediaStore

internal object MediaStoreConfig {

    object Song {

        val Collection: Uri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

        val Projection = buildList {
            add(MediaStore.Audio.Media._ID)
            add(MediaStore.Audio.Media.ARTIST_ID)
            add(MediaStore.Audio.Media.ALBUM_ID)
            add(MediaStore.Audio.Media.TITLE)
            add(MediaStore.Audio.Media.ARTIST)
            add(MediaStore.Audio.Media.ALBUM)
            add(MediaStore.Audio.Media.DATE_ADDED)
            add(MediaStore.Audio.Media.DURATION)
            add(MediaStore.Audio.Media.DATA)
            add(MediaStore.Audio.Media.BITRATE)
            add(MediaStore.Audio.Media.SIZE)

            if (SdkExtensions.getExtensionVersion(Build.VERSION_CODES.TIRAMISU) >= 15) {
                add(MediaStore.Audio.Media.BITS_PER_SAMPLE)
                add(MediaStore.Audio.Media.SAMPLERATE)
            }
            add(MediaStore.Audio.Media.TRACK)
            add(MediaStore.Audio.Media.YEAR)
        }.toTypedArray()
    }
}
