package ru.resodostudio.muzyakich.feature.settings.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.resodostudio.muzyakich.core.navigation.Navigator

@Serializable
data object LicensesNavKey : NavKey

fun Navigator.navigateToLicenses() = navigate(LicensesNavKey)