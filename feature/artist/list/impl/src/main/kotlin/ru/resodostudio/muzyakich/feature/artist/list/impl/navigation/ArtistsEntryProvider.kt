package ru.resodostudio.muzyakich.feature.artist.list.impl.navigation

import androidx.compose.runtime.Composable
import ru.resodostudio.muzyakich.feature.artist.list.impl.ArtistsScreen

@Composable
fun ArtistsEntry(onArtistClick: (Long) -> Unit) {
    ArtistsScreen(
        onArtistClick = onArtistClick,
    )
}
