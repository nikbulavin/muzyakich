package ru.resodostudio.muzyakich

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.tracing.trace
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.MainActivityUiState.Loading
import ru.resodostudio.muzyakich.core.designsystem.theme.MuzTheme
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.ui.MuzApp
import ru.resodostudio.muzyakich.ui.rememberMuzAppState
import ru.resodostudio.muzyakich.util.isSystemInDarkTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var musicServiceConnection: MusicServiceConnection

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var themeSettings by mutableStateOf(
            ThemeSettings(
                darkTheme = resources.configuration.isSystemInDarkTheme,
                dynamicTheme = Loading.shouldUseDynamicTheming,
            ),
        )

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    isSystemInDarkTheme(),
                    viewModel.uiState,
                ) { systemDark, uiState ->
                    ThemeSettings(
                        darkTheme = uiState.shouldUseDarkTheme(systemDark),
                        dynamicTheme = uiState.shouldUseDynamicTheming,
                    )
                }
                    .onEach { themeSettings = it }
                    .map { it.darkTheme }
                    .distinctUntilChanged()
                    .collect { darkTheme ->
                        trace("muzEdgeToEdge") {
                            enableEdgeToEdge(
                                statusBarStyle = SystemBarStyle.auto(
                                    lightScrim = Color.Transparent.toArgb(),
                                    darkScrim = Color.Transparent.toArgb(),
                                ) { darkTheme },
                                navigationBarStyle = SystemBarStyle.auto(
                                    lightScrim = Color.Transparent.toArgb(),
                                    darkScrim = Color.Transparent.toArgb(),
                                ) { darkTheme },
                            )
                        }
                    }
            }
        }

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.shouldKeepSplashScreen() }

        setContent {
            val appState = rememberMuzAppState(
                musicServiceConnection = musicServiceConnection,
            )
            MuzTheme(
                darkTheme = themeSettings.darkTheme,
                dynamicTheme = themeSettings.dynamicTheme,
            ) {
                MuzApp(appState)
            }
        }
    }
}

data class ThemeSettings(
    val darkTheme: Boolean,
    val dynamicTheme: Boolean,
)