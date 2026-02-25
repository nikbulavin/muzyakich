package ru.resodostudio.muzyakich.core.mediastore.util

import android.database.Cursor
import android.os.Build
import android.os.ext.SdkExtensions
import android.provider.MediaStore

internal fun Cursor.getDataFolder() = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)).asFolder()

internal fun Cursor.getMediaId() = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media._ID))

internal fun Cursor.getArtistId() = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID))

internal fun Cursor.getAlbumId() = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))

internal fun Cursor.getTitle() = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))

internal fun Cursor.getArtist() = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))

internal fun Cursor.getAlbum() = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))

internal fun Cursor.getDuration() = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))

internal fun Cursor.getBitrate() = getInt(getColumnIndexOrThrow(MediaStore.Audio.Media.BITRATE))

internal fun Cursor.getIsFavorite() =
    getInt(getColumnIndexOrThrow(MediaStore.Audio.Media.IS_FAVORITE))

internal fun Cursor.getSize() = getInt(getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
internal fun Cursor.getBitsPerSample() =
    if (SdkExtensions.getExtensionVersion(Build.VERSION_CODES.TIRAMISU) >= 15) {
        getInt(getColumnIndexOrThrow(MediaStore.Audio.Media.BITS_PER_SAMPLE))
    } else {
        0
    }

internal fun Cursor.getSampleRate() =
    if (SdkExtensions.getExtensionVersion(Build.VERSION_CODES.TIRAMISU) >= 15) {
        getInt(getColumnIndexOrThrow(MediaStore.Audio.Media.SAMPLERATE))
    } else {
        0
    }

internal fun Cursor.getTrackNumber() = getInt(getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK))

internal fun Cursor.getYear() = getInt(getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR))