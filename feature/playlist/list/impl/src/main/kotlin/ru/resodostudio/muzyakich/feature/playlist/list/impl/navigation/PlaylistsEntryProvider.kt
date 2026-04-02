package ru.resodostudio.muzyakich.feature.playlist.list.impl.navigation

import androidx.compose.runtime.Composable
import ru.resodostudio.muzyakich.feature.playlist.list.impl.PlaylistsScreen
import kotlin.uuid.Uuid

@Composable
fun PlaylistsEntry(onPlaylistClick: (Uuid) -> Unit) {
    PlaylistsScreen(
        onPlaylistClick = onPlaylistClick,
    )
}