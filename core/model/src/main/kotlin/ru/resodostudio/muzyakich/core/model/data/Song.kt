package ru.resodostudio.muzyakich.core.model.data

import android.net.Uri
import kotlin.uuid.Uuid

data class Song(
    val uuid: Uuid,
    val mediaId: String,
    val artistId: Long,
    val albumId: Long,
    val mediaUri: Uri,
    val artworkUri: Uri,
    val title: String,
    val artist: String,
    val album: String,
    val folder: String,
    val duration: Long,
    val bitrate: Int,
    val isFavorite: Boolean,
    val size: Int,
    val bitsPerSample: Int,
    val sampleRate: Int,
    val trackNumber: Int,
    val year: Int,
    val genre: String?,
)