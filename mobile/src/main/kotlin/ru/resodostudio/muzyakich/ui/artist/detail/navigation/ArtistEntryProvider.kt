package ru.resodostudio.muzyakich.ui.artist.detail.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.ui.artist.detail.ArtistScreen
import ru.resodostudio.muzyakich.ui.artist.detail.ArtistViewModel

fun EntryProviderScope<NavKey>.artistEntry(navigator: Navigator) {
    entry<ArtistNavKey> { artistKey ->
        ArtistScreen(
            onBackClick = navigator::goBack,
            viewModel = hiltViewModel<ArtistViewModel, ArtistViewModel.Factory> {
                it.create(artistKey.artistId)
            },
        )
    }
}