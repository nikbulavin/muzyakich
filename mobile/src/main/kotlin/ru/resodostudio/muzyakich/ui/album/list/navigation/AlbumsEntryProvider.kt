package ru.resodostudio.muzyakich.ui.album.list.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.ui.album.list.AlbumsScreen

fun EntryProviderScope<NavKey>.albumsEntry(onAlbumClick: (Long) -> Unit) {
    entry<AlbumsNavKey> {
        AlbumsScreen(
            onAlbumClick = onAlbumClick,
        )
    }
}