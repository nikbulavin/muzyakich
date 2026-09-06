package ru.resodostudio.muzyakich.feature.song.editor.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.resodostudio.muzyakich.core.navigation.Navigator

@Serializable
data class SongEditorNavKey(
    val mediaId: String,
) : NavKey

fun Navigator.navigateToSongEditor(mediaId: String) = navigate(SongEditorNavKey(mediaId))
