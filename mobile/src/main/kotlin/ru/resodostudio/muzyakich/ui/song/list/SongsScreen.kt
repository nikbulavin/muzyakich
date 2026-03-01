package ru.resodostudio.muzyakich.ui.song.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import ru.resodostudio.muzyakich.ui.component.EmptyState
import ru.resodostudio.muzyakich.ui.component.LoadingState
import ru.resodostudio.muzyakich.ui.component.songs
import ru.resodostudio.muzyakich.ui.component.songsInfo
import ru.resodostudio.muzyakich.ui.library.actionButtons
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun SongsScreen(
    modifier: Modifier = Modifier,
    viewModel: SongsViewModel = hiltViewModel(),
) {
    val songsUiState by viewModel.songsUiState.collectAsStateWithLifecycle()

    SongsScreen(
        songsUiState = songsUiState,
        modifier = modifier,
        onPlaySongsClick = viewModel::playSongs,
        onShuffleSongsClick = viewModel::shuffleSongs,
        onToggleFilterFavorites = viewModel::toggleFilterFavorites,
        onPlayNextClick = viewModel::playSongNext,
        onSortByUpdate = viewModel::updateSortByPreference,
        onSortOrderUpdate = viewModel::updateSortOrderPreference,
        onFavoriteChange = viewModel::setSongFavorite,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SongsScreen(
    songsUiState: SongsUiState,
    modifier: Modifier = Modifier,
    onPlaySongsClick: (songs: List<Song>, startIndex: Int) -> Unit = { _, _ -> },
    onShuffleSongsClick: (songs: List<Song>, startIndex: Int) -> Unit = { _, _ -> },
    onToggleFilterFavorites: (Boolean) -> Unit = {},
    onPlayNextClick: (Song) -> Unit = {},
    onSortByUpdate: (SortBy) -> Unit = {},
    onSortOrderUpdate: (SortOrder) -> Unit = {},
    onFavoriteChange: (String, Boolean) -> Unit = { _, _ -> },
) {
    when (songsUiState) {
        SongsUiState.Empty -> {
            EmptyState(
                messageRes = localesR.string.library_empty,
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp)
                    .navigationBarsPadding(),
            )
        }

        SongsUiState.Loading -> LoadingState(modifier.fillMaxSize())
        is SongsUiState.Success -> {
            var shouldShowFilterBottomSheet by rememberSaveable { mutableStateOf(false) }

            if (shouldShowFilterBottomSheet) {
                FilterBottomSheet(
                    filterConfig = songsUiState.filterConfig,
                    onSortByUpdate = onSortByUpdate,
                    onSortOrderUpdate = onSortOrderUpdate,
                    onDismiss = { shouldShowFilterBottomSheet = false },
                    onToggleFilterFavorites = onToggleFilterFavorites,
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 104.dp + WindowInsets.navigationBars.asPaddingValues()
                        .calculateBottomPadding(),
                ),
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
            ) {
                actionButtons(
                    songs = songsUiState.songs,
                    onPlaySongsClick = onPlaySongsClick,
                    onShuffleSongsClick = onShuffleSongsClick,
                    onFilterClick = { shouldShowFilterBottomSheet = true },
                )
                songs(
                    songs = songsUiState.songs,
                    currentMediaId = songsUiState.currentMediaId,
                    onPlaySongsClick = onPlaySongsClick,
                    onPlayNextClick = onPlayNextClick,
                    isPlaying = songsUiState.isPlaying,
                    onFavoriteChange = onFavoriteChange,
                )
                songsInfo(
                    songs = songsUiState.songs,
                )
            }
        }
    }
}