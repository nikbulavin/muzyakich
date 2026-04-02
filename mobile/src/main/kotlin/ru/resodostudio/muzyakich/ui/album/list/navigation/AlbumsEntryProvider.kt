package ru.resodostudio.muzyakich.ui.album.list.navigation

import androidx.compose.runtime.Composable
import ru.resodostudio.muzyakich.ui.album.list.AlbumsScreen

@Composable
fun AlbumsEntry(onAlbumClick: (Long) -> Unit) {
    AlbumsScreen(
        onAlbumClick = onAlbumClick,
    )
}