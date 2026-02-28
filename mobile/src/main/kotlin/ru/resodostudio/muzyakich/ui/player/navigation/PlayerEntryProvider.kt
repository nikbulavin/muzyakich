package ru.resodostudio.muzyakich.ui.player.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MotionScheme
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.ui.player.PlayerScreen

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun EntryProviderScope<NavKey>.playerEntry(navigator: Navigator, motionScheme: MotionScheme) {
    entry<PlayerNavKey>(
        metadata = NavDisplay.transitionSpec {
            fadeIn(motionScheme.defaultEffectsSpec()) + slideInVertically(motionScheme.slowSpatialSpec()) { it } togetherWith
                    fadeOut(motionScheme.defaultEffectsSpec()) + slideOutVertically(motionScheme.defaultSpatialSpec()) { -it }
        } + NavDisplay.popTransitionSpec {
            fadeIn(motionScheme.defaultEffectsSpec()) + slideInVertically(motionScheme.slowSpatialSpec()) { -it } togetherWith
                    fadeOut(motionScheme.defaultEffectsSpec()) + slideOutVertically(motionScheme.defaultSpatialSpec()) { -it }
        } + NavDisplay.predictivePopTransitionSpec {
            fadeIn(motionScheme.defaultEffectsSpec()) + slideInVertically(motionScheme.slowSpatialSpec()) { -it } togetherWith
                    fadeOut(motionScheme.defaultEffectsSpec()) + slideOutVertically(motionScheme.defaultSpatialSpec()) { it }
        },
    ) {
        PlayerScreen(
            onBackClick = navigator::goBack,
        )
    }
}