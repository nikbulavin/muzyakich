package ru.resodostudio.muzyakich.ui.artist

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.resodostudio.muzyakich.ui.component.LoadingState

@Composable
fun ArtistScreen(
    onBackClick: () -> Unit,
    viewModel: ArtistViewModel = hiltViewModel(),
) {
    val artistUiState = viewModel.artistUiState.collectAsStateWithLifecycle()

    ArtistScreen(
        artistUiState = artistUiState.value,
        onBackClick = onBackClick,
    )
}

@Composable
fun ArtistScreen(
    artistUiState: ArtistUiState,
    onBackClick: () -> Unit,
) {
    when (artistUiState) {
        ArtistUiState.Error -> LoadingState()
        ArtistUiState.Loading -> LoadingState()
        is ArtistUiState.Success -> {
            Text(text = artistUiState.artist.name)
        }
    }
}