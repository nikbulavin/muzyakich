package ru.resodostudio.muzyakich.feature.artist.list.impl.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import ru.resodostudio.muzyakich.feature.artist.list.impl.ArtistsScreen

@Composable
fun ArtistsEntry(
    onArtistClick: (Long) -> Unit,
    innerPadding: PaddingValues,
) {
    ArtistsScreen(
        onArtistClick = onArtistClick,
        innerPadding = innerPadding,
    )
}
