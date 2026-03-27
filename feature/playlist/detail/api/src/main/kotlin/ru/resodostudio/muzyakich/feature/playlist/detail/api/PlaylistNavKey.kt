package ru.resodostudio.muzyakich.feature.playlist.detail.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.resodostudio.muzyakich.core.navigation.Navigator
import kotlin.uuid.Uuid

@Serializable
data class PlaylistNavKey(
    val playlistUuid: Uuid,
) : NavKey

fun Navigator.navigateToPlaylist(playlistUuid: Uuid) = navigate(PlaylistNavKey(playlistUuid))