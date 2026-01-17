package ru.resodostudio.muzyakich.ui.player.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.resodostudio.muzyakich.core.navigation.Navigator

@Serializable
data object PlayerNavKey : NavKey

fun Navigator.navigateToPlayer() = navigate(PlayerNavKey)