package ru.resodostudio.muzyakich.feature.playlist.editor.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.resodostudio.muzyakich.core.navigation.Navigator
import kotlin.uuid.Uuid

@Serializable
data class PlaylistEditorNavKey(
    val playlistUuid: Uuid?,
) : NavKey

fun Navigator.navigateToPlaylistEditor(playlistUuid: Uuid? = null) {
    navigate(PlaylistEditorNavKey(playlistUuid))
}
