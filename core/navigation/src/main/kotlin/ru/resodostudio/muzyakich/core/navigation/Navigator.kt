package ru.resodostudio.muzyakich.core.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events (forward and back) by updating the navigation state.
 *
 * @param state - The navigation state that will be updated in response to navigation events.
 */
class Navigator(val state: NavigationState) {

    /**
     * Navigate to a navigation key
     *
     * @param key - the navigation key to navigate to.
     */
    fun navigate(key: NavKey) {
        state.backStack.apply {
            // Remove it if it's already in the stack so it's added at the end.
            remove(key)
            add(key)
        }
    }

    /**
     * Go back to the previous navigation key.
     */
    fun goBack() {
        if (state.currentKey != state.startKey) {
            state.backStack.removeLastOrNull()
        }
    }
}
