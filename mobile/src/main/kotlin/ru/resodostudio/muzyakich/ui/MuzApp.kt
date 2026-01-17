package ru.resodostudio.muzyakich.ui

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.shouldShowRationale
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.core.navigation.toEntries
import ru.resodostudio.muzyakich.ui.artist.navigation.artistEntry
import ru.resodostudio.muzyakich.ui.library.navigation.libraryEntry
import ru.resodostudio.muzyakich.ui.player.NowPlayingBar
import ru.resodostudio.muzyakich.ui.player.navigation.PlayerNavKey
import ru.resodostudio.muzyakich.ui.player.navigation.navigateToPlayer
import ru.resodostudio.muzyakich.ui.player.navigation.playerEntry

@OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalHazeMaterialsApi::class,
    ExperimentalHazeApi::class,
    ExperimentalMaterial3ExpressiveApi::class,
)
@Composable
fun MuzApp(
    appState: MuzAppState,
) {
    val navigator = remember { Navigator(appState.navigationState) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            )
        },
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
        ) {
            val hazeState = rememberHazeState()
            val nowPlayingBarHazeStyle = HazeMaterials.ultraThin(MaterialTheme.colorScheme.surfaceContainer)
            val hazeBlurRadius = 32.dp

            val permissionState = appState.permissionState
            when (permissionState.status) {
                is PermissionStatus.Denied -> {
                    LaunchedEffect(permissionState) {
                        if (!permissionState.status.shouldShowRationale) {
                            permissionState.launchPermissionRequest()
                        }
                    }
                }

                PermissionStatus.Granted -> {
                    val motionScheme = MaterialTheme.motionScheme

                    val entryProvider = entryProvider {
                        libraryEntry(navigator)
                        playerEntry(navigator)
                        artistEntry(navigator)
                    }

                    NavDisplay(
                        modifier = Modifier.hazeSource(hazeState),
                        entries = appState.navigationState.toEntries(entryProvider),
                        onBack = navigator::goBack,
                        transitionSpec = {
                            ContentTransform(
                                fadeIn(motionScheme.defaultEffectsSpec()) + slideInVertically(motionScheme.defaultSpatialSpec()) { it / 2 },
                                fadeOut(motionScheme.fastEffectsSpec()) + slideOutVertically(motionScheme.defaultSpatialSpec()),
                            )
                        },
                        popTransitionSpec = {
                            ContentTransform(
                                fadeIn(motionScheme.defaultEffectsSpec()) + slideInVertically(motionScheme.defaultSpatialSpec()),
                                fadeOut(motionScheme.fastEffectsSpec()) + slideOutVertically(motionScheme.defaultSpatialSpec()),
                            )
                        },
                        predictivePopTransitionSpec = {
                            ContentTransform(
                                fadeIn(motionScheme.defaultEffectsSpec()) + slideInVertically(motionScheme.defaultSpatialSpec()),
                                fadeOut(motionScheme.fastEffectsSpec()) + slideOutVertically(motionScheme.defaultSpatialSpec()) { it / 2 },
                            )
                        },
                    )
                }
            }
            NowPlayingBar(
                isPlayerScreenOpened = appState.navigationState.currentKey is PlayerNavKey,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(16.dp)
                    .dropShadow(
                        shape = MaterialTheme.shapes.medium,
                        shadow = Shadow(
                            radius = 10.dp,
                            spread = 6.dp,
                            color = MaterialTheme.colorScheme.inverseSurface,
                            alpha = 0.1f,
                        ),
                    )
                    .align(Alignment.BottomCenter)
                    .clip(MaterialTheme.shapes.medium)
                    .hazeEffect(hazeState, nowPlayingBarHazeStyle) {
                        inputScale = HazeInputScale.Auto
                        blurEnabled = true
                        blurRadius = hazeBlurRadius
                        noiseFactor = 0f
                    },
                onClick = navigator::navigateToPlayer,
            )
        }
    }
}