package ru.resodostudio.muzyakich.ui.settings

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.data.repository.UserDataRepository
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@OptIn(UnstableApi::class)
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    val settingsUiState: StateFlow<SettingsUiState> = combine(
        userDataRepository.userData,
        musicServiceConnection.audioSessionId,
    ) { userData, audioSessionId ->
        SettingsUiState.Success(
            darkThemeConfig = userData.darkThemeConfig,
            useDynamicColor = userData.useDynamicColor,
            audioSessionId = audioSessionId,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = SettingsUiState.Loading,
        )

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            userDataRepository.setDarkThemeConfig(darkThemeConfig)
        }
    }

    fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        viewModelScope.launch {
            userDataRepository.setDynamicColorPreference(useDynamicColor)
        }
    }
}

sealed interface SettingsUiState {

    data object Loading : SettingsUiState

    data class Success(
        val darkThemeConfig: DarkThemeConfig,
        val useDynamicColor: Boolean,
        val audioSessionId: Int?,
    ) : SettingsUiState
}