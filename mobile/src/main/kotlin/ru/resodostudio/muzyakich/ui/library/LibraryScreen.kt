package ru.resodostudio.muzyakich.ui.library

import androidx.compose.foundation.layout.fillMaxWidth
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
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Album
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Artist
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.LibraryMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun LibraryScreen() {
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
}

data class TabItem(
    val title: String,
    val icon: ImageVector,
)