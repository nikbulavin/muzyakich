package ru.resodostudio.muzyakich.ui.library

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Album
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.LibraryMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.feature.album.list.api.AlbumsNavKey
import ru.resodostudio.muzyakich.feature.playlist.list.api.PlaylistsNavKey
import ru.resodostudio.muzyakich.feature.song.list.api.SongsNavKey
import ru.resodostudio.muzyakich.ui.artist.list.navigation.ArtistsNavKey
import ru.resodostudio.muzyakich.core.locales.R as localesR

enum class LibraryTab(
    @StringRes val titleRes: Int,
    val icon: ImageVector,
    val navKey: NavKey,
) {
    PLAYLISTS(
        titleRes = localesR.string.core_locales_playlists,
        icon = MuzIcons.Rounded.LibraryMusic,
        navKey = PlaylistsNavKey,
    ),
    SONGS(
        titleRes = localesR.string.core_locales_songs,
        icon = MuzIcons.Rounded.MusicNote,
        navKey = SongsNavKey,
    ),
    ALBUMS(
        titleRes = localesR.string.core_locales_albums,
        icon = MuzIcons.Rounded.Album,
        navKey = AlbumsNavKey,
    ),
    ARTISTS(
        titleRes = localesR.string.core_locales_artists,
        icon = MuzIcons.Rounded.Artist,
        navKey = ArtistsNavKey,
    ),
}
