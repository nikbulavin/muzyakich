package ru.resodostudio.muzyakich.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@Composable
fun rememberMuzAppState(): MuzAppState {
    return remember {
        MuzAppState()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Stable
class MuzAppState() {

    val permissionState: PermissionState
        @Composable get() = rememberMuzyakichPermissionState { isPermissionRequested = true }

    var isPermissionRequested by mutableStateOf(false)
        private set
}