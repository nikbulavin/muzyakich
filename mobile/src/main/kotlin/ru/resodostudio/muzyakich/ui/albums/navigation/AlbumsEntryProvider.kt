package ru.resodostudio.muzyakich.ui.albums.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.ui.albums.AlbumsScreen

fun EntryProviderScope<NavKey>.albumsEntry() {
    entry<AlbumsNavKey> {
        AlbumsScreen()
    }
}