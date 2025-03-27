package ru.resodostudio.muzyakich.ui

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberMuzyakichPermissionState(
    onPermissionResult: (Boolean) -> Unit = {},
) = rememberPermissionState(
    permission = muzyakichAudioPermission,
    onPermissionResult = onPermissionResult,
)

private val muzyakichAudioPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    Manifest.permission.READ_MEDIA_AUDIO
} else {
    Manifest.permission.READ_EXTERNAL_STORAGE
}