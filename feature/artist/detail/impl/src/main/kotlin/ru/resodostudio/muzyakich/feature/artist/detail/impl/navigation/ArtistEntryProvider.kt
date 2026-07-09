package ru.resodostudio.muzyakich.feature.artist.detail.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.artist.detail.api.ArtistNavKey
import ru.resodostudio.muzyakich.feature.artist.detail.impl.ArtistScreen
import ru.resodostudio.muzyakich.feature.artist.detail.impl.ArtistViewModel
import ru.resodostudio.muzyakich.feature.song.detail.api.navigateToSong

fun EntryProviderScope<NavKey>.artistEntry(
    navigator: Navigator,
) {
    entry<ArtistNavKey> { artistKey ->
        ArtistScreen(
            onBackClick = navigator::goBack,
            onSongMenuClick = navigator::navigateToSong,
            viewModel = hiltViewModel<ArtistViewModel, ArtistViewModel.Factory> {
                it.create(artistKey.artistId)
            },
        )
    }
}
