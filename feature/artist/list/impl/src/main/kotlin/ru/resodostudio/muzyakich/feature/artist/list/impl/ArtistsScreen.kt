package ru.resodostudio.muzyakich.feature.artist.list.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSelectableListItem
import ru.resodostudio.muzyakich.core.model.Artist
import ru.resodostudio.muzyakich.core.ui.EmptyState
import ru.resodostudio.muzyakich.core.ui.LoadingState
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun ArtistsScreen(
    onArtistClick: (Long) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: ArtistsViewModel = hiltViewModel(),
) {
    val artistsUiState by viewModel.artistsUiState.collectAsStateWithLifecycle()

    ArtistsScreen(
        artistsUiState = artistsUiState,
        onArtistClick = onArtistClick,
        innerPadding = innerPadding,
        modifier = modifier,
    )
}

@Composable
private fun ArtistsScreen(
    artistsUiState: ArtistsUiState,
    onArtistClick: (Long) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    when (artistsUiState) {
        ArtistsUiState.Empty -> {
            EmptyState(
                messageRes = localesR.string.core_locales_library_empty,
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp)
                    .padding(innerPadding),
            )
        }

        ArtistsUiState.Loading -> {
            LoadingState(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }

        is ArtistsUiState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
                contentPadding = innerPadding + PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 140.dp,
                ) + WindowInsets.navigationBars.asPaddingValues(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                artists(
                    artists = artistsUiState.artists,
                    onArtistClick = onArtistClick,
                )
            }
        }
    }
}

private fun LazyGridScope.artists(
    artists: List<Artist>,
    onArtistClick: (Long) -> Unit,
) {
    itemsIndexed(
        items = artists,
        key = { _, artist -> artist.id },
        contentType = { _, _ -> "Artist" },
    ) { index, artist ->
        MuzSelectableListItem(
            shapes = ListItemDefaults.segmentedShapes(index, artists.size),
            selected = false,
            content = {
                Text(
                    text = artist.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            supportingContent = {
                Text(
                    text = pluralStringResource(
                        localesR.plurals.core_locales_number_of_songs,
                        artist.songs.size,
                        artist.songs.size,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            onClick = dropUnlessResumed { onArtistClick(artist.id) },
            modifier = Modifier.animateItem(),
        )
    }
}
