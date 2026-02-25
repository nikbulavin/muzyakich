package ru.resodostudio.muzyakich.ui.library.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.ui.album.navigation.navigateToAlbum
import ru.resodostudio.muzyakich.ui.artist.navigation.navigateToArtist
import ru.resodostudio.muzyakich.ui.library.LibraryScreen

fun EntryProviderScope<NavKey>.libraryEntry(navigator: Navigator) {
    entry<LibraryNavKey> {
        LibraryScreen(
            onAlbumClick = navigator::navigateToAlbum,
            onArtistClick = navigator::navigateToArtist,
        )
    }
}