package ru.resodostudio.muzyakich.feature.song.editor.impl

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.resodostudio.muzyakich.core.data.repository.SongMetadataRepository
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.model.SongMetadata
import java.io.ByteArrayOutputStream

@HiltViewModel(assistedFactory = SongEditorViewModel.Factory::class)
internal class SongEditorViewModel @AssistedInject constructor(
    @Assisted val mediaId: String,
    @ApplicationContext private val context: Context,
    private val songsRepository: SongsRepository,
    private val songMetadataRepository: SongMetadataRepository,
) : ViewModel() {

    val songEditorUiState: StateFlow<SongEditorUiState>
        field = MutableStateFlow<SongEditorUiState>(SongEditorUiState.Loading)

    init {
        loadSongTags()
    }

    private fun loadSongTags() {
        viewModelScope.launch {
            val song = songsRepository.getSong(mediaId).firstOrNull()
            if (song == null) {
                songEditorUiState.value = SongEditorUiState.Error
                return@launch
            }

            val audioTag = songMetadataRepository.getSongMetadata(song.path)
            songEditorUiState.value = SongEditorUiState.Success(
                mediaId = mediaId,
                filePath = song.path,
                mediaUri = song.mediaUri,
                title = audioTag?.title.takeUnless { it.isNullOrBlank() } ?: song.title,
                artist = audioTag?.artist.takeUnless { it.isNullOrBlank() } ?: song.artist,
                album = audioTag?.album.takeUnless { it.isNullOrBlank() } ?: song.album,
                albumArtist = audioTag?.albumArtist.orEmpty(),
                year = audioTag?.year.takeUnless { it.isNullOrBlank() } ?: song.year?.toString()
                    .orEmpty(),
                genre = audioTag?.genre.takeUnless { it.isNullOrBlank() } ?: song.genre.orEmpty(),
                trackNumber = audioTag?.trackNumber.takeUnless { it.isNullOrBlank() }
                    ?: song.trackNumber.takeIf { it > 0 }?.toString().orEmpty(),
                discNumber = audioTag?.discNumber.orEmpty(),
                comment = audioTag?.comment.orEmpty(),
                coverModel = audioTag?.artworkBytes ?: song.artworkUri,
                artworkBytes = audioTag?.artworkBytes,
            )
        }
    }

    fun onTitleChange(title: String) {
        updateState { copy(title = title) }
    }

    fun onArtistChange(artist: String) {
        updateState { copy(artist = artist) }
    }

    fun onAlbumChange(album: String) {
        updateState { copy(album = album) }
    }

    fun onAlbumArtistChange(albumArtist: String) {
        updateState { copy(albumArtist = albumArtist) }
    }

    fun onYearChange(year: String) {
        updateState { copy(year = year) }
    }

    fun onGenreChange(genre: String) {
        updateState { copy(genre = genre) }
    }

    fun onTrackNumberChange(trackNumber: String) {
        updateState { copy(trackNumber = trackNumber) }
    }

    fun onDiscNumberChange(discNumber: String) {
        updateState { copy(discNumber = discNumber) }
    }

    fun onCommentChange(comment: String) {
        updateState { copy(comment = comment) }
    }

    fun updateCover(uri: Uri?) {
        if (uri == null) return
        viewModelScope.launch {
            val compressedBytes = withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        ByteArrayOutputStream().use { outputStream ->
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                            outputStream.toByteArray()
                        }
                    }
                }.getOrNull()
            }

            if (compressedBytes != null) {
                updateState {
                    copy(
                        coverModel = uri,
                        artworkBytes = compressedBytes,
                        isArtworkChanged = true,
                    )
                }
            }
        }
    }

    fun removeCover() {
        updateState {
            copy(
                coverModel = null,
                artworkBytes = null,
                isArtworkChanged = true,
            )
        }
    }

    fun saveTags(onSuccess: () -> Unit) {
        val currentState = songEditorUiState.value as? SongEditorUiState.Success ?: return
        if (currentState.isSaving) return

        updateState { copy(isSaving = true) }

        viewModelScope.launch {
            val tag = SongMetadata(
                title = currentState.title,
                artist = currentState.artist,
                album = currentState.album,
                albumArtist = currentState.albumArtist,
                year = currentState.year,
                genre = currentState.genre,
                trackNumber = currentState.trackNumber,
                discNumber = currentState.discNumber,
                comment = currentState.comment,
                artworkBytes = currentState.artworkBytes,
                isArtworkChanged = currentState.isArtworkChanged,
            )

            val result = songMetadataRepository.updateSongMetadata(
                filePath = currentState.filePath,
                mediaUri = currentState.mediaUri,
                songMetadata = tag,
            )

            result.onSuccess {
                updateState { copy(isSaving = false) }
                onSuccess()
            }.onFailure { exception ->
                val isSecurityException = exception is SecurityException ||
                        exception.javaClass.name.contains("RecoverableSecurityException") ||
                        (exception.localizedMessage?.contains(
                            "Permission denied",
                            ignoreCase = true
                        ) == true)

                val pendingIntent = if (isSecurityException) {
                    runCatching {
                        MediaStore.createWriteRequest(
                            context.contentResolver,
                            listOf(currentState.mediaUri.toUri()),
                        )
                    }.getOrNull()
                } else {
                    null
                }

                if (pendingIntent != null) {
                    updateState { copy(isSaving = false, pendingWriteIntent = pendingIntent) }
                } else {
                    updateState {
                        copy(
                            isSaving = false,
                            errorMsg = exception.localizedMessage ?: "Error saving tags",
                        )
                    }
                }
            }
        }
    }

    fun onWritePermissionHandled() {
        updateState { copy(pendingWriteIntent = null) }
    }

    private inline fun updateState(transform: SongEditorUiState.Success.() -> SongEditorUiState.Success) {
        songEditorUiState.update { currentState ->
            if (currentState is SongEditorUiState.Success) {
                currentState.transform()
            } else {
                currentState
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            mediaId: String,
        ): SongEditorViewModel
    }
}

sealed interface SongEditorUiState {

    data object Loading : SongEditorUiState

    data object Error : SongEditorUiState

    data class Success(
        val mediaId: String,
        val filePath: String,
        val mediaUri: String,
        val title: String,
        val artist: String,
        val album: String,
        val albumArtist: String,
        val year: String,
        val genre: String,
        val trackNumber: String,
        val discNumber: String,
        val comment: String,
        val coverModel: Any?,
        val isArtworkChanged: Boolean = false,
        val artworkBytes: ByteArray? = null,
        val isSaving: Boolean = false,
        val errorMsg: String? = null,
        val pendingWriteIntent: PendingIntent? = null,
    ) : SongEditorUiState
}
