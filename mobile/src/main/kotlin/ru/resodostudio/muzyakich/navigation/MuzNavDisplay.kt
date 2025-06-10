package ru.resodostudio.muzyakich.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ru.resodostudio.muzyakich.core.designsystem.theme.LocalSharedTransitionScope
import ru.resodostudio.muzyakich.ui.library.LibraryScreen
import ru.resodostudio.muzyakich.ui.player.PlayerScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MuzNavDisplay() {
    val backStack = rememberNavBackStack(LibraryRoute)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            val sharedTransitionScope = LocalSharedTransitionScope.current
            with(sharedTransitionScope) {
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
            }
        },
    )
}