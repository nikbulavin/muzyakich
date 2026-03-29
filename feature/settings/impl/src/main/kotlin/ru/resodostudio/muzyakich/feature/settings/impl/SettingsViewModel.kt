package ru.resodostudio.muzyakich.feature.settings.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.data.repository.UserDataRepository
import ru.resodostudio.muzyakich.core.data.repository.util.AppLocaleManager
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    musicServiceConnection: MusicServiceConnection,
    private val appLocaleManager: AppLocaleManager,
) : ViewModel() {

    private val _languageState = MutableStateFlow(appLocaleManager.getCurrentLanguage())

    val settingsUiState: StateFlow<SettingsUiState> = combine(
        userDataRepository.userData,
        musicServiceConnection.audioSessionId,
        _languageState.asStateFlow(),
    ) { userData, audioSessionId, languageTag ->
        SettingsUiState.Success(
            darkThemeConfig = userData.darkThemeConfig,
            useDynamicColor = userData.useDynamicColor,
            audioSessionId = audioSessionId,
            languageTag = languageTag,
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

    fun setAppLanguage(languageTag: String) {
        appLocaleManager.setAppLanguage(languageTag)
        _languageState.value = languageTag
    }
}

sealed interface SettingsUiState {

    data object Loading : SettingsUiState

    data class Success(
        val darkThemeConfig: DarkThemeConfig,
        val useDynamicColor: Boolean,
        val audioSessionId: Int?,
        val languageTag: String,
    ) : SettingsUiState
}