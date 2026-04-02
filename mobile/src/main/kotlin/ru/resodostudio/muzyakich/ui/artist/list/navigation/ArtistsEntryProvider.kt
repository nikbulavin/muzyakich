package ru.resodostudio.muzyakich.ui.artist.list.navigation

import androidx.compose.runtime.Composable
import ru.resodostudio.muzyakich.ui.artist.list.ArtistsScreen

@Composable
fun ArtistsEntry(onArtistClick: (Long) -> Unit) {
    ArtistsScreen(
        onArtistClick = onArtistClick,
    )
}