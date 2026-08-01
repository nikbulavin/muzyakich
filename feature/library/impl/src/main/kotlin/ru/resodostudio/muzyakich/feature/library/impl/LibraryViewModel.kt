package ru.resodostudio.muzyakich.feature.library.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.ktx.AppUpdateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.data.repository.util.InAppUpdateManager
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class LibraryViewModel @Inject constructor(
    inAppUpdateManager: InAppUpdateManager,
) : ViewModel() {

    val libraryUiState: StateFlow<LibraryUiState> = inAppUpdateManager.inAppUpdateResult
        .map { appUpdateResult ->
            LibraryUiState.Success(
                appUpdateResult = appUpdateResult,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = LibraryUiState.Loading,
        )
}

internal sealed interface LibraryUiState {

    data object Loading : LibraryUiState

    data object Error : LibraryUiState

    data class Success(
        val appUpdateResult: AppUpdateResult,
    ) : LibraryUiState
}
