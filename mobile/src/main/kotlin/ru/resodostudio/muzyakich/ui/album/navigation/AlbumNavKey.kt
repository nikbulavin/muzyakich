package ru.resodostudio.muzyakich.ui.album.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.resodostudio.muzyakich.core.navigation.Navigator

@Serializable
data class AlbumNavKey(val albumId: Long) : NavKey

fun Navigator.navigateToAlbum(albumId: Long) = navigate(AlbumNavKey(albumId))