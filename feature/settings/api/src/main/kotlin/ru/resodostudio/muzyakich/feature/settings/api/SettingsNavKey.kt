package ru.resodostudio.muzyakich.ui.settings.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.resodostudio.muzyakich.core.navigation.Navigator

@Serializable
data object SettingsNavKey : NavKey

fun Navigator.navigateToSettings() = navigate(SettingsNavKey)