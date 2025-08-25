package ru.resodostudio.muzyakich.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object LibraryRoute : NavKey

@Serializable
data object PlayerRoute : NavKey

@Serializable
data class ArtistRoute(val artistId: Long) : NavKey