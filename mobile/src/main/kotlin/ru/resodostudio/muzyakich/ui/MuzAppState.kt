package ru.resodostudio.muzyakich.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import ru.resodostudio.muzyakich.core.media.service.MusicServiceConnection
import ru.resodostudio.muzyakich.core.model.data.NowPlayingState
import ru.resodostudio.muzyakich.core.navigation.NavigationState
import ru.resodostudio.muzyakich.core.navigation.rememberNavigationState
import ru.resodostudio.muzyakich.ui.library.navigation.LibraryNavKey
import kotlin.time.Duration.Companion.seconds

@Composable
fun rememberMuzAppState(
    musicServiceConnection: MusicServiceConnection,
    navigationState: NavigationState = rememberNavigationState(listOf(LibraryNavKey)),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): MuzAppState {
    return remember(
        musicServiceConnection,
        navigationState,
        coroutineScope,
    ) {
        MuzAppState(
            musicServiceConnection = musicServiceConnection,
            navigationState = navigationState,
            coroutineScope = coroutineScope,
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Stable
class MuzAppState(
    val musicServiceConnection: MusicServiceConnection,
    val navigationState: NavigationState,
    coroutineScope: CoroutineScope,
) {

    val permissionState: PermissionState
        @Composable get() = rememberMuzyakichPermissionState { isPermissionRequested = true }

    var isPermissionRequested by mutableStateOf(false)
        private set

    val nowPlayingState = musicServiceConnection.nowPlayingState
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = NowPlayingState(),
        )
}