package ru.resodostudio.muzyakich.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.core.navigation.toEntries
import ru.resodostudio.muzyakich.ui.MuzAppState
import ru.resodostudio.muzyakich.ui.artist.ArtistScreen
import ru.resodostudio.muzyakich.ui.artist.ArtistViewModel
import ru.resodostudio.muzyakich.ui.library.LibraryScreen
import ru.resodostudio.muzyakich.ui.player.navigation.navigateToPlayer
import ru.resodostudio.muzyakich.ui.player.navigation.playerEntry

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MuzNavDisplay(
    appState: MuzAppState,
) {
    val navigator = remember { Navigator(appState.navigationState) }
    val motionScheme = MaterialTheme.motionScheme

    val entryProvider = entryProvider {
        entry<LibraryNavKey> {
            LibraryScreen(
                onNowPlayingBarClick = navigator::navigateToPlayer,
                onArtistClick = { artistId -> navigator.navigate(ArtistNavKey(artistId)) },
            )
        }
        playerEntry(navigator)
        entry<ArtistNavKey> { artistKey ->
            ArtistScreen(
                onBackClick = navigator::goBack,
                viewModel = hiltViewModel<ArtistViewModel, ArtistViewModel.Factory>(
                    key = artistKey.artistId.toString(),
                ) { factory ->
                    factory.create(artistKey.artistId)
                },
            )
        }
    }

    NavDisplay(
        entries = appState.navigationState.toEntries(entryProvider),
        onBack = navigator::goBack,
        transitionSpec = {
            ContentTransform(
                fadeIn(motionScheme.defaultEffectsSpec()) + slideInVertically(motionScheme.defaultSpatialSpec()) { it / 2 },
                fadeOut(motionScheme.fastEffectsSpec()) + slideOutVertically(motionScheme.defaultSpatialSpec()),
            )
        },
        popTransitionSpec = {
            ContentTransform(
                fadeIn(motionScheme.defaultEffectsSpec()) + slideInVertically(motionScheme.defaultSpatialSpec()),
                fadeOut(motionScheme.fastEffectsSpec()) + slideOutVertically(motionScheme.defaultSpatialSpec()),
            )
        },
        predictivePopTransitionSpec = {
            ContentTransform(
                fadeIn(motionScheme.defaultEffectsSpec()) + slideInVertically(motionScheme.defaultSpatialSpec()),
                fadeOut(motionScheme.fastEffectsSpec()) + slideOutVertically(motionScheme.defaultSpatialSpec()) { it / 2 },
            )
        },
    )
}