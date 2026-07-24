package ru.resodostudio.muzyakich.feature.library.impl.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Album
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.LibraryMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Album
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.LibraryMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.locales.R
import ru.resodostudio.muzyakich.feature.album.list.api.AlbumsNavKey
import ru.resodostudio.muzyakich.feature.artist.list.api.ArtistsNavKey
import ru.resodostudio.muzyakich.feature.playlist.list.api.PlaylistsNavKey
import ru.resodostudio.muzyakich.feature.song.list.api.SongsNavKey

enum class LibraryTab(
    @StringRes val titleRes: Int,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val navKey: NavKey,
) {
    PLAYLISTS(
        titleRes = R.string.core_locales_playlists,
        unselectedIcon = MuzIcons.Rounded.LibraryMusic,
        selectedIcon = MuzIcons.Filled.LibraryMusic,
        navKey = PlaylistsNavKey,
    ),
    SONGS(
        titleRes = R.string.core_locales_songs,
        unselectedIcon = MuzIcons.Rounded.MusicNote,
        selectedIcon = MuzIcons.Rounded.MusicNote,
        navKey = SongsNavKey,
    ),
    ALBUMS(
        titleRes = R.string.core_locales_albums,
        unselectedIcon = MuzIcons.Rounded.Album,
        selectedIcon = MuzIcons.Filled.Album,
        navKey = AlbumsNavKey,
    ),
    ARTISTS(
        titleRes = R.string.core_locales_artists,
        unselectedIcon = MuzIcons.Rounded.Artist,
        selectedIcon = MuzIcons.Filled.Artist,
        navKey = ArtistsNavKey,
    ),
}