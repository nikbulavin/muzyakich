package ru.resodostudio.muzyakich.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
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

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
)
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
                fadeIn(motionScheme.defaultEffectsSpec()),
                fadeOut(motionScheme.defaultEffectsSpec()),
            )
        },
        popTransitionSpec = {
            ContentTransform(
                fadeIn(motionScheme.defaultEffectsSpec()),
                scaleOut(
                    animationSpec = motionScheme.defaultSpatialSpec(),
                    targetScale = 0.7f,
                ),
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
                PlayerScreen()
            }
        },
    )
}