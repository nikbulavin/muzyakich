package ru.resodostudio.muzyakich.feature.song.list.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.resodostudio.cashsense.core.ui.EmptyState
import ru.resodostudio.cashsense.core.ui.LoadingState
import ru.resodostudio.cashsense.core.ui.songs
import ru.resodostudio.cashsense.core.ui.songsInfo
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.FilterList
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlayArrow
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Shuffle
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun SongsScreen(
    onSongMenuClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SongsViewModel = hiltViewModel(),
) {
    val songsUiState by viewModel.songsUiState.collectAsStateWithLifecycle()

    SongsScreen(
        songsUiState = songsUiState,
        onSongMenuClick = onSongMenuClick,
        modifier = modifier,
        onPlaySongsClick = viewModel::playSongs,
        onToggleFilterFavorites = viewModel::toggleFilterFavorites,
        onSortByUpdate = viewModel::updateSortByPreference,
        onSortOrderUpdate = viewModel::updateSortOrderPreference,
        onSongLeftToRightSwipe = viewModel::playSongNext,
        onSongRemove = viewModel::removeSong,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SongsScreen(
    songsUiState: SongsUiState,
    onSongMenuClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onPlaySongsClick: (songs: List<Song>, startIndex: Int, shuffle: Boolean) -> Unit = { _, _, _ -> },
    onToggleFilterFavorites: (Boolean) -> Unit = {},
    onSortByUpdate: (SortBy) -> Unit = {},
    onSortOrderUpdate: (SortOrder) -> Unit = {},
    onSongLeftToRightSwipe: (Song) -> Unit = {},
    onSongRemove: (String) -> Unit = {},
) {
    when (songsUiState) {
        SongsUiState.Empty -> {
            EmptyState(
                messageRes = localesR.string.core_locales_library_empty,
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp)
                    .navigationBarsPadding(),
            )
        }

        SongsUiState.Loading -> {
            LoadingState(
                modifier = modifier
                    .fillMaxSize()
                    .navigationBarsPadding(),
            )
        }

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
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                actionButtons(
                    songs = songsUiState.songs,
                    onPlaySongsClick = onPlaySongsClick,
                    onFilterClick = { shouldShowFilterBottomSheet = true },
                )
                songs(
                    songs = songsUiState.songs,
                    currentMediaId = songsUiState.currentMediaId,
                    onPlaySongsClick = { songs, index -> onPlaySongsClick(songs, index, false) },
                    isPlaying = songsUiState.isPlaying,
                    onSongMenuClick = onSongMenuClick,
                    onSongLeftToRightSwipe = onSongLeftToRightSwipe,
                    onSongRemove = onSongRemove,
                )
                songsInfo(
                    songs = songsUiState.songs,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun LazyGridScope.actionButtons(
    songs: List<Song>,
    onPlaySongsClick: (List<Song>, Int, Boolean) -> Unit,
    onFilterClick: () -> Unit,
) {
    item(
        span = { GridItemSpan(maxLineSpan) },
        contentType = { "ActionButtons" },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp - ListItemDefaults.SegmentedGap)
                .animateItem(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                shapes = ButtonDefaults.shapes(),
                onClick = { onPlaySongsClick(songs, DEFAULT_INDEX, false) },
                modifier = Modifier.weight(1f),
                contentPadding = ButtonDefaults.contentPaddingFor(
                    buttonHeight = ButtonDefaults.MinHeight,
                    hasStartIcon = true,
                ),
            ) {
                Icon(
                    imageVector = MuzIcons.Rounded.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.iconSizeFor(ButtonDefaults.MinHeight)),
                )
                Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(ButtonDefaults.MinHeight)))
                Text(
                    text = stringResource(localesR.string.core_locales_play_audio),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            OutlinedButton(
                shapes = ButtonDefaults.shapes(),
                onClick = { onPlaySongsClick(songs, DEFAULT_INDEX, true) },
                modifier = Modifier.weight(1f),
                contentPadding = ButtonDefaults.contentPaddingFor(
                    buttonHeight = ButtonDefaults.MinHeight,
                    hasStartIcon = true,
                ),
            ) {
                Icon(
                    imageVector = MuzIcons.Rounded.Shuffle,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.iconSizeFor(ButtonDefaults.MinHeight)),
                )
                Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(ButtonDefaults.MinHeight)))
                Text(
                    text = stringResource(localesR.string.core_locales_shuffle),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            MuzIconButton(
                onClick = onFilterClick,
                icon = MuzIcons.Rounded.FilterList,
                contentDescription = stringResource(localesR.string.core_locales_open_filter_menu),
            )
        }
    }
}