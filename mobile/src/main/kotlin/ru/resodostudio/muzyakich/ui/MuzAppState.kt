package ru.resodostudio.muzyakich.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import ru.resodostudio.muzyakich.core.navigation.NavigationState
import ru.resodostudio.muzyakich.core.navigation.rememberNavigationState
import ru.resodostudio.muzyakich.ui.library.navigation.LibraryNavKey

@Composable
fun rememberMuzAppState(
    navigationState: NavigationState = rememberNavigationState(listOf(LibraryNavKey)),
): MuzAppState {
    return remember {
        MuzAppState(
            navigationState = navigationState,
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Stable
class MuzAppState(
    val navigationState: NavigationState,
) {

    val permissionState: PermissionState
        @Composable get() = rememberMuzyakichPermissionState { isPermissionRequested = true }

    var isPermissionRequested by mutableStateOf(false)
        private set
}