package ru.resodostudio.muzyakich.feature.artist.detail.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.resodostudio.muzyakich.core.navigation.Navigator

@Serializable
data class ArtistNavKey(val artistId: Long) : NavKey

fun Navigator.navigateToArtist(artistId: Long) = navigate(ArtistNavKey(artistId))
