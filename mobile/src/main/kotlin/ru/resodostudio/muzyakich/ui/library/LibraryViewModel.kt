package ru.resodostudio.muzyakich.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.ktx.AppUpdateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.data.repository.util.InAppUpdateManager
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class LibraryViewModel @Inject constructor(
    inAppUpdateManager: InAppUpdateManager,
    musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    val libraryUiState: StateFlow<LibraryUiState> = combine(
        inAppUpdateManager.inAppUpdateResult.onStart { emit(AppUpdateResult.NotAvailable) },
        musicServiceConnection.nowPlayingState,
    ) { appUpdateResult, nowPlayingState ->
        LibraryUiState.Success(
            appUpdateResult = appUpdateResult,
            isCurrentMediaItemExists = nowPlayingState.player?.currentMediaItem != null,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = LibraryUiState.Loading,
        )
}

sealed interface LibraryUiState {

    data object Loading : LibraryUiState

    data object Error : LibraryUiState

    data class Success(
        val appUpdateResult: AppUpdateResult,
        val isCurrentMediaItemExists: Boolean,
    ) : LibraryUiState
}