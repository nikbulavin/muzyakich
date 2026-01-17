package ru.resodostudio.muzyakich.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.core.navigation.toEntries
import ru.resodostudio.muzyakich.ui.MuzAppState
import ru.resodostudio.muzyakich.ui.artist.navigation.artistEntry
import ru.resodostudio.muzyakich.ui.library.navigation.libraryEntry
import ru.resodostudio.muzyakich.ui.player.navigation.playerEntry

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MuzNavDisplay(
    appState: MuzAppState,
) {
    val navigator = remember { Navigator(appState.navigationState) }
    val motionScheme = MaterialTheme.motionScheme

    val entryProvider = entryProvider {
        libraryEntry(navigator)
        playerEntry(navigator)
        artistEntry(navigator)
    }

    NavDisplay(
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