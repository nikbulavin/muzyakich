package ru.resodostudio.muzyakich.ui.library

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import ru.resodostudio.muzyakich.core.model.data.Song

@Composable
fun SongItem(
    song: Song,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(
                text = song.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(
                text = song.artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
    )
}