package ru.resodostudio.muzyakich.feature.library.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.album.detail.api.navigateToAlbum
import ru.resodostudio.muzyakich.feature.artist.detail.api.navigateToArtist
import ru.resodostudio.muzyakich.feature.library.api.LibraryNavKey
import ru.resodostudio.muzyakich.feature.library.impl.LibraryScreen
import ru.resodostudio.muzyakich.feature.playlist.detail.api.navigateToPlaylist
import ru.resodostudio.muzyakich.feature.settings.api.navigateToSettings
import ru.resodostudio.muzyakich.feature.song.detail.api.navigateToSong

fun EntryProviderScope<NavKey>.libraryEntry(
    navigator: Navigator,
    libraryNavigator: Navigator,
) {
    entry<LibraryNavKey> {
        LibraryScreen(
            libraryNavigator = libraryNavigator,
            onPlaylistClick = navigator::navigateToPlaylist,
            onAlbumClick = navigator::navigateToAlbum,
            onArtistClick = navigator::navigateToArtist,
            onSongMenuClick = navigator::navigateToSong,
            onSettingsClick = navigator::navigateToSettings,
        )
    }
}
