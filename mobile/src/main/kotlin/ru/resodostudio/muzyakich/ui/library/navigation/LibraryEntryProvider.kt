package ru.resodostudio.muzyakich.ui.library.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.playlist.editor.api.navigateToPlaylistEditor
import ru.resodostudio.muzyakich.feature.settings.api.navigateToSettings
import ru.resodostudio.muzyakich.feature.song.detail.api.navigateToSong
import ru.resodostudio.muzyakich.ui.album.detail.navigation.navigateToAlbum
import ru.resodostudio.muzyakich.ui.artist.detail.navigation.navigateToArtist
import ru.resodostudio.muzyakich.ui.library.LibraryScreen

fun EntryProviderScope<NavKey>.libraryEntry(navigator: Navigator) {
    entry<LibraryNavKey> {
        LibraryScreen(
            onPlaylistClick = {},
            onNewPlaylistClick = navigator::navigateToPlaylistEditor,
            onAlbumClick = navigator::navigateToAlbum,
            onArtistClick = navigator::navigateToArtist,
            onSongMenuClick = navigator::navigateToSong,
            onSettingsClick = navigator::navigateToSettings,
        )
    }
}