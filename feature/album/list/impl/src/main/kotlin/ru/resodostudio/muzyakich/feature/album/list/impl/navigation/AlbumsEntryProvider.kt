package ru.resodostudio.muzyakich.feature.album.list.impl.navigation

import androidx.compose.runtime.Composable
import ru.resodostudio.muzyakich.feature.album.list.impl.AlbumsScreen

@Composable
fun AlbumsEntry(onAlbumClick: (Long) -> Unit) {
    AlbumsScreen(
        onAlbumClick = onAlbumClick,
    )
}
