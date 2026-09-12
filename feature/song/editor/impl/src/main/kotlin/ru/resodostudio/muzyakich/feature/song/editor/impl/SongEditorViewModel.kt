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
import java.io.File

@HiltViewModel(assistedFactory = SongEditorViewModel.Factory::class)
internal class SongEditorViewModel @AssistedInject constructor(
    @Assisted val mediaId: String,
    @ApplicationContext private val context: Context,
    private val songsRepository: SongsRepository,
    private val songMetadataRepository: SongMetadataRepository,
) : ViewModel() {

    val songEditorUiState: StateFlow<SongEditorUiState>
        field = MutableStateFlow(SongEditorUiState())

    init {
        loadSongTags()
    }

    private fun loadSongTags() {
        viewModelScope.launch {
            val song = songsRepository.getSong(mediaId).firstOrNull()
            if (song == null) {
                songEditorUiState.update { it.copy(isLoading = false, isError = true) }
                return@launch
            }

            val tag = songMetadataRepository.getSongMetadata(song.path)
            val metadata = SongMetadata(
                title = tag?.title?.ifBlank { null } ?: song.title,
                artist = tag?.artist?.ifBlank { null } ?: song.artist,
                album = tag?.album?.ifBlank { null } ?: song.album,
                albumArtist = tag?.albumArtist.orEmpty(),
                year = tag?.year?.ifBlank { null } ?: song.year?.toString().orEmpty(),
                genre = tag?.genre?.ifBlank { null } ?: song.genre.orEmpty(),
                trackNumber = tag?.trackNumber?.ifBlank { null }
                    ?: song.trackNumber.takeIf { it > 0 }?.toString().orEmpty(),
                discNumber = tag?.discNumber.orEmpty(),
                comment = tag?.comment.orEmpty(),
                artworkUri = tag?.artworkUri ?: song.artworkUri,
            )

            songEditorUiState.update {
                it.copy(
                    isLoading = false,
                    filePath = song.path,
                    mediaUri = song.mediaUri,
                    metadata = metadata,
                    isArtworkChanged = false,
                )
            }
        }
    }

    fun updateMetadata(metadata: SongMetadata) {
        songEditorUiState.update { it.copy(metadata = metadata) }
    }

    fun updateCover(uri: Uri?) {
        if (uri == null) return
        viewModelScope.launch {
            val compressedUri = withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        val cacheFile = File(context.cacheDir, "edit_artwork_${System.currentTimeMillis()}.jpg")
                        cacheFile.outputStream().use { output ->
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, output)
                        }
                        cacheFile.toUri().toString()
                    }
                }.getOrNull()
            } ?: uri.toString()

            songEditorUiState.update { state ->
                state.metadata?.let { meta ->
                    state.copy(
                        metadata = meta.copy(artworkUri = compressedUri),
                        isArtworkChanged = true,
                    )
                } ?: state
            }
        }
    }

    fun removeCover() {
        songEditorUiState.update { state ->
            state.metadata?.let { meta ->
                state.copy(
                    metadata = meta.copy(artworkUri = null),
                    isArtworkChanged = true,
                )
            } ?: state
        }
    }

    fun saveTags(onSuccess: () -> Unit) {
        val state = songEditorUiState.value
        val metadata = state.metadata ?: return
        if (state.isSaving || state.isLoading) return

        songEditorUiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            val result = songMetadataRepository.updateSongMetadata(
                filePath = state.filePath,
                mediaUri = state.mediaUri,
                songMetadata = metadata,
                isArtworkChanged = state.isArtworkChanged,
            )

            result.onSuccess {
                songEditorUiState.update { it.copy(isSaving = false) }
                onSuccess()
            }.onFailure { exception ->
                val isSecurityException = exception is SecurityException ||
                        exception.javaClass.name.contains("RecoverableSecurityException") ||
                        (exception.localizedMessage?.contains("Permission denied", ignoreCase = true) == true)

                val pendingIntent = if (isSecurityException) {
                    runCatching {
                        MediaStore.createWriteRequest(
                            context.contentResolver,
                            listOf(state.mediaUri.toUri()),
                        )
                    }.getOrNull()
                } else {
                    null
                }

                songEditorUiState.update {
                    it.copy(
                        isSaving = false,
                        pendingWriteIntent = pendingIntent,
                        errorMsg = if (pendingIntent == null) exception.localizedMessage ?: "Error saving tags" else null,
                    )
                }
            }
        }
    }

    fun onWritePermissionHandled() {
        songEditorUiState.update { it.copy(pendingWriteIntent = null) }
    }

    @AssistedFactory
    interface Factory {
        fun create(mediaId: String): SongEditorViewModel
    }
}

data class SongEditorUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val isSaving: Boolean = false,
    val metadata: SongMetadata? = null,
    val isArtworkChanged: Boolean = false,
    val filePath: String = "",
    val mediaUri: String = "",
    val errorMsg: String? = null,
    val pendingWriteIntent: PendingIntent? = null,
) {
    val isSaveEnabled: Boolean
        get() = !isLoading && !isSaving && !metadata?.title.isNullOrBlank()
}
