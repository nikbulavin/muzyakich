package ru.resodostudio.muzyakich.core.data.repository.impl

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.provider.MediaStore
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.images.ArtworkFactory
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.IO
import ru.resodostudio.muzyakich.core.data.repository.SongMetadataRepository
import ru.resodostudio.muzyakich.core.model.SongMetadata
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

internal class SongMetadataRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : SongMetadataRepository {

    override suspend fun getSongMetadata(filePath: String): SongMetadata? = withContext(ioDispatcher) {
        runCatching {
            val file = File(filePath)
            if (!file.exists()) return@runCatching null

            val audioFile = AudioFileIO.read(file)
            val tag = audioFile.tag

            val artworkUri = tag?.firstArtwork?.binaryData?.let { bytes ->
                context.cacheDir.listFiles { _, name -> name.startsWith("edit_artwork_") }?.forEach {
                    it.delete()
                }
                val cacheFile = File(context.cacheDir, "edit_artwork_${System.currentTimeMillis()}.jpg")
                cacheFile.writeBytes(bytes)
                cacheFile.toUri().toString()
            }

            SongMetadata(
                title = tag?.getFirst(FieldKey.TITLE).orEmpty(),
                artist = tag?.getFirst(FieldKey.ARTIST).orEmpty(),
                album = tag?.getFirst(FieldKey.ALBUM).orEmpty(),
                albumArtist = tag?.getFirst(FieldKey.ALBUM_ARTIST).orEmpty(),
                year = tag?.getFirst(FieldKey.YEAR).orEmpty(),
                genre = tag?.getFirst(FieldKey.GENRE).orEmpty(),
                trackNumber = tag?.getFirst(FieldKey.TRACK).orEmpty(),
                discNumber = tag?.getFirst(FieldKey.DISC_NO).orEmpty(),
                comment = tag?.getFirst(FieldKey.COMMENT).orEmpty(),
                artworkUri = artworkUri,
            )
        }.getOrNull()
    }

    override suspend fun updateSongMetadata(
        filePath: String,
        mediaUri: String,
        songMetadata: SongMetadata,
    ): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val uri = mediaUri.toUri()
            val extension = File(filePath).extension.let { if (it.isNotEmpty()) ".$it" else "" }
            val tempFile = File.createTempFile("tag_edit_", extension, context.cacheDir)

            try {
                val copiedFromUri = runCatching {
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        tempFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }.isSuccess

                if (!copiedFromUri || tempFile.length() == 0L) {
                    val file = File(filePath)
                    require(file.exists()) { "File not found: $filePath" }
                    file.copyTo(tempFile, overwrite = true)
                }

                val audioFile = AudioFileIO.read(tempFile)
                val audioTag = audioFile.tag ?: audioFile.createDefaultTag()
                audioFile.tag = audioTag

                audioTag.setField(FieldKey.TITLE, songMetadata.title)
                audioTag.setField(FieldKey.ARTIST, songMetadata.artist)
                audioTag.setField(FieldKey.ALBUM, songMetadata.album)
                audioTag.setField(FieldKey.ALBUM_ARTIST, songMetadata.albumArtist)
                audioTag.setField(FieldKey.YEAR, songMetadata.year)
                audioTag.setField(FieldKey.GENRE, songMetadata.genre)
                audioTag.setField(FieldKey.TRACK, songMetadata.trackNumber)
                audioTag.setField(FieldKey.DISC_NO, songMetadata.discNumber)
                audioTag.setField(FieldKey.COMMENT, songMetadata.comment)

                val uriString = songMetadata.artworkUri
                if (uriString.isNullOrBlank()) {
                    audioTag.deleteArtworkField()
                } else {
                    val bytes = readArtworkBytes(uriString)
                    if (bytes != null && bytes.isNotEmpty()) {
                        audioTag.deleteArtworkField()
                        val artwork = ArtworkFactory.getNew()
                        artwork.binaryData = bytes
                        audioTag.setField(artwork)
                    }
                }

                AudioFileIO.write(audioFile)

                val pfd = context.contentResolver.openFileDescriptor(uri, "rwt")
                    ?: context.contentResolver.openFileDescriptor(uri, "rw")
                    ?: throw IOException("Cannot open file descriptor for $uri")

                pfd.use { descriptor ->
                    FileOutputStream(descriptor.fileDescriptor).use { out ->
                        tempFile.inputStream().use { input ->
                            input.copyTo(out)
                        }
                    }
                }

                runCatching {
                    val values = ContentValues().apply {
                        if (songMetadata.title.isNotBlank()) {
                            put(MediaStore.Audio.Media.TITLE, songMetadata.title)
                        }
                        if (songMetadata.artist.isNotBlank()) {
                            put(MediaStore.Audio.Media.ARTIST, songMetadata.artist)
                        }
                        if (songMetadata.album.isNotBlank()) {
                            put(MediaStore.Audio.Media.ALBUM, songMetadata.album)
                        }
                        songMetadata.year.toIntOrNull()?.let {
                            put(MediaStore.Audio.Media.YEAR, it)
                        }
                        songMetadata.trackNumber.toIntOrNull()?.let {
                            put(MediaStore.Audio.Media.TRACK, it)
                        }
                        put(
                            MediaStore.Audio.Media.DATE_MODIFIED,
                            System.currentTimeMillis() / 1000,
                        )
                    }
                    context.contentResolver.update(uri, values, null, null)
                }

                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(filePath),
                    null,
                    null,
                )
            } finally {
                tempFile.delete()
                context.cacheDir.listFiles { _, name -> name.startsWith("edit_artwork_") }?.forEach {
                    it.delete()
                }
            }
        }
    }

    private fun readArtworkBytes(uriString: String): ByteArray? = runCatching {
        val uri = uriString.toUri()
        if (uri.scheme == null || uri.scheme == "file") {
            val path = uri.path ?: uriString
            File(path).readBytes()
        } else {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        }
    }.getOrNull()
}
