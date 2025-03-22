package ru.resodostudio.muzyakich

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.data.repository.UserDataRepository
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig.DARK
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig.FOLLOW_SYSTEM
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig.LIGHT
import ru.resodostudio.muzyakich.core.model.data.UserData
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData
        .map<UserData, MainActivityUiState>(MainActivityUiState::Success)
        .stateIn(
            scope = viewModelScope,
            initialValue = MainActivityUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )
}

sealed interface MainActivityUiState {

    data object Loading : MainActivityUiState

    data class Success(val userData: UserData) : MainActivityUiState {
        override val shouldUseDynamicTheming = userData.useDynamicColor

        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) =
            when (userData.darkThemeConfig) {
                FOLLOW_SYSTEM -> isSystemDarkTheme
                LIGHT -> false
                DARK -> true
            }
    }

    fun shouldKeepSplashScreen() = this is Loading

    val shouldUseDynamicTheming: Boolean get() = false

    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = isSystemDarkTheme
}