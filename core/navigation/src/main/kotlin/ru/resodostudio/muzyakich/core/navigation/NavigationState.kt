package ru.resodostudio.muzyakich.core.navigation

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

@Composable
fun rememberNavigationState(
    initialBackStack: List<NavKey>,
): NavigationState {
    require(initialBackStack.isNotEmpty()) { "Initial back stack cannot be empty" }

    val backStack = rememberNavBackStack(*initialBackStack.toTypedArray())

    return remember(backStack) {
        NavigationState(
            backStack = backStack,
        )
    }
}

/**
 * State holder for navigation state.
 *
 * @param backStack - the back stack.
 */
class NavigationState(
    val backStack: NavBackStack<NavKey>,
) {
    val startKey: NavKey
        get() = backStack.first()

    @get:VisibleForTesting
    val currentKey: NavKey
        get() = backStack.last()
}

/**
 * Convert NavigationState into NavEntries.
 */
@Composable
fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): List<NavEntry<NavKey>> {
    return rememberDecoratedNavEntries(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider,
    )
}
