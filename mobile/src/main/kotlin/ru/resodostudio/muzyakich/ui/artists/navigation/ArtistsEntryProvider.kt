package ru.resodostudio.muzyakich.ui.artists.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.ui.artists.ArtistsScreen

fun EntryProviderScope<NavKey>.artistsEntry(onArtistClick: (Long) -> Unit) {
    entry<ArtistsNavKey> {
        ArtistsScreen(
            onArtistClick = onArtistClick,
        )
    }
}