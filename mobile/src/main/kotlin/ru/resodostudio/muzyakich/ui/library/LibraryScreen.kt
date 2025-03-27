package ru.resodostudio.muzyakich.ui.library

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Album
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.LibraryMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val libraryUiState by viewModel.libraryUiState.collectAsStateWithLifecycle()

    LibraryScreen(
        libraryUiState = libraryUiState,
    )
}

@Composable
private fun LibraryScreen(
    libraryUiState: LibraryUiState,
) {
    val tabs = listOf(
        TabItem(stringResource(localesR.string.playlists), MuzIcons.Rounded.LibraryMusic),
        TabItem(stringResource(localesR.string.songs), MuzIcons.Rounded.MusicNote),
        TabItem(stringResource(localesR.string.albums), MuzIcons.Rounded.Album),
        TabItem(stringResource(localesR.string.artists), MuzIcons.Rounded.Artist),
    )
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    PrimaryScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.fillMaxWidth(),
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = null,
                    )
                },
                text = {
                    Text(
                        text = tab.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
            )
        }
    }
    when (libraryUiState) {
        LibraryUiState.Loading -> CircularProgressIndicator()
        LibraryUiState.Empty -> Text(text = "Empty")
        is LibraryUiState.Success -> {
            LazyColumn {
                items(libraryUiState.songs) { song ->
                    Text(text = song.title)
                }
            }
        }
    }
}

data class TabItem(
    val title: String,
    val icon: ImageVector,
)