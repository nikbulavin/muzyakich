package ru.resodostudio.muzyakich.feature.playlist.detail.impl

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun PlaylistScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel = hiltViewModel(),
) {
    val playlistsUiState by viewModel.playlistsUiState.collectAsStateWithLifecycle()

    PlaylistScreen(
        playlistsUiState = playlistsUiState,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PlaylistScreen(
    playlistsUiState: PlaylistsUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

}