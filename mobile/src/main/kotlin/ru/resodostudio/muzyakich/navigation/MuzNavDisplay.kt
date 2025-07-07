package ru.resodostudio.muzyakich.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ru.resodostudio.muzyakich.ui.library.LibraryScreen
import ru.resodostudio.muzyakich.ui.player.PlayerScreen

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MuzNavDisplay() {
    val backStack = rememberNavBackStack(LibraryRoute)
    val motionScheme = MaterialTheme.motionScheme

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
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
        entryProvider = entryProvider {
            entry<LibraryRoute> {
                LibraryScreen(
                    onNowPlayingBarClick = {
                        backStack.add(PlayerRoute)
                    },
                )
            }
            entry<PlayerRoute> {
                PlayerScreen(
                    onBackClick = {
                        backStack.removeLastOrNull()
                    },
                )
            }
        },
    )
}