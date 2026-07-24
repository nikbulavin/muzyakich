package ru.resodostudio.muzyakich.feature.album.list.impl.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import ru.resodostudio.muzyakich.feature.album.list.impl.AlbumsScreen

@Composable
fun AlbumsEntry(
    onAlbumClick: (Long) -> Unit,
    innerPadding: PaddingValues,
) {
    AlbumsScreen(
        onAlbumClick = onAlbumClick,
        innerPadding = innerPadding,
    )
}
