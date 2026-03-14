package ru.resodostudio.muzyakich.core.mediastore.util

import android.database.Cursor
import android.os.Build
import android.os.ext.SdkExtensions
import android.provider.MediaStore

internal fun Cursor.getPath() = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

internal fun Cursor.getMediaId() = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media._ID))

internal fun Cursor.getArtistId() = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID))

internal fun Cursor.getAlbumId() = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))

internal fun Cursor.getTitle() = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))

internal fun Cursor.getArtist() = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))

internal fun Cursor.getAlbum() = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))

internal fun Cursor.getDuration() = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))

internal fun Cursor.getBitrate() = getInt(getColumnIndexOrThrow(MediaStore.Audio.Media.BITRATE))

internal fun Cursor.getSize() = getInt(getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))

internal fun Cursor.getYear() = getInt(getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR))

internal fun Cursor.getBitsPerSample(): Int? {
    return if (SdkExtensions.getExtensionVersion(Build.VERSION_CODES.TIRAMISU) >= 15) {
        getInt(getColumnIndexOrThrow(MediaStore.Audio.Media.BITS_PER_SAMPLE)).takeIf { it != 0 }
    } else {
        null
    }
}

internal fun Cursor.getSampleRate(): Int? {
    return if (SdkExtensions.getExtensionVersion(Build.VERSION_CODES.TIRAMISU) >= 15) {
        getInt(getColumnIndexOrThrow(MediaStore.Audio.Media.SAMPLERATE)).takeIf { it != 0 }
    } else {
        null
    }
}

internal fun Cursor.getTrackNumber() = getInt(getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK))

internal fun Cursor.getGenre(): String? {
    val index = getColumnIndex(MediaStore.Audio.Media.GENRE)
    return if (index != -1) getString(index) else null
}