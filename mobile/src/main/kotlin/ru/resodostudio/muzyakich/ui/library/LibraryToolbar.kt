package ru.resodostudio.muzyakich.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlayArrow
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Shuffle
import ru.resodostudio.muzyakich.core.model.data.Song
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
internal fun LibraryToolbar(
    expanded: Boolean,
    onPlaySongsClick: (List<Song>, Int) -> Unit,
    onShuffleSongsClick: (List<Song>, Int) -> Unit,
    songs: List<Song>,
    modifier: Modifier = Modifier,
) {
    HorizontalFloatingToolbar(
        modifier = modifier,
        expanded = expanded,
        trailingContent = {
            TextButton(
                shapes = ButtonDefaults.shapes(),
                onClick = { onShuffleSongsClick(songs, DEFAULT_INDEX) },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ButtonDefaults.IconSpacing),
                ) {
                    Icon(
                        imageVector = MuzIcons.Rounded.Shuffle,
                        contentDescription = null,
                    )
                    Text(text = stringResource(localesR.string.shuffle))
                }
            }
        },
        content = {
            Button(
                shapes = ButtonDefaults.shapes(),
                onClick = { onPlaySongsClick(songs, DEFAULT_INDEX) },
                modifier = Modifier.padding(end = if (expanded) 8.dp else 0.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(ButtonDefaults.IconSpacing),
                ) {
                    Icon(
                        imageVector = MuzIcons.Rounded.PlayArrow,
                        contentDescription = null,
                    )
                    Text(text = stringResource(localesR.string.play_audio))
                }
            }
        },
    )
}