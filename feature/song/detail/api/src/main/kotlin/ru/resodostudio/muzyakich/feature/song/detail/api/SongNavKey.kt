package ru.resodostudio.muzyakich.feature.song.detail.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.resodostudio.muzyakich.core.navigation.Navigator

@Serializable
data class SongNavKey(
    val mediaId: String,
) : NavKey

fun Navigator.navigateToSong(mediaId: String) = navigate(SongNavKey(mediaId))