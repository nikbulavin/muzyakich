package ru.resodostudio.muzyakich.feature.player.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.resodostudio.muzyakich.core.navigation.Navigator

@Serializable
data object PlayerNavKey : NavKey

fun Navigator.navigateToPlayer() = navigate(PlayerNavKey)