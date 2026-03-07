package ru.resodostudio.muzyakich.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.ktx.AppUpdateResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.data.repository.util.InAppUpdateManager
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class LibraryViewModel @Inject constructor(
    inAppUpdateManager: InAppUpdateManager,
) : ViewModel() {

    val inAppUpdateState = inAppUpdateManager.inAppUpdateResult
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = AppUpdateResult.NotAvailable,
        )
}