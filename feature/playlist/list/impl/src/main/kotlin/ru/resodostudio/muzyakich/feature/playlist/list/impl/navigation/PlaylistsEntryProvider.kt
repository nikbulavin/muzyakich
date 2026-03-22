package ru.resodostudio.muzyakich.feature.playlist.list.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.feature.playlist.list.api.PlaylistsNavKey
import ru.resodostudio.muzyakich.feature.playlist.list.impl.PlaylistsScreen
import kotlin.uuid.Uuid

fun EntryProviderScope<NavKey>.playlistsEntry(onPlaylistClick: (Uuid) -> Unit) {
    entry<PlaylistsNavKey> {
        PlaylistsScreen(
            onPlaylistClick = onPlaylistClick,
        )
    }
}