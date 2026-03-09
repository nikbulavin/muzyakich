package ru.resodostudio.muzyakich.ui.library.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.ui.album.detail.navigation.navigateToAlbum
import ru.resodostudio.muzyakich.ui.artist.detail.navigation.navigateToArtist
import ru.resodostudio.muzyakich.ui.library.LibraryScreen
import ru.resodostudio.muzyakich.ui.settings.navigation.navigateToSettings
import ru.resodostudio.muzyakich.ui.song.detail.navigation.navigateToSong

fun EntryProviderScope<NavKey>.libraryEntry(navigator: Navigator) {
    entry<LibraryNavKey> {
        LibraryScreen(
            onAlbumClick = navigator::navigateToAlbum,
            onArtistClick = navigator::navigateToArtist,
            onSongMenuClick = navigator::navigateToSong,
            onSettingsClick = navigator::navigateToSettings,
        )
    }
}