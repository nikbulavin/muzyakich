package ru.resodostudio.muzyakich.ui.song.detail.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class SongNavKey(
    val mediaId: String,
) : NavKey
