package ru.resodostudio.muzyakich.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object LibraryNavKey : NavKey

@Serializable
data class ArtistNavKey(val artistId: Long) : NavKey