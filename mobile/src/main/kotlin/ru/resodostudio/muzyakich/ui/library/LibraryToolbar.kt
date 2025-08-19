package ru.resodostudio.muzyakich.ui.library

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.common.Constants.DEFAULT_INDEX
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MoreVert
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
    val vibrantColors = FloatingToolbarDefaults.vibrantFloatingToolbarColors()
    HorizontalFloatingToolbar(
        colors = vibrantColors,
        modifier = modifier,
        expanded = expanded,
        content = {
            Spacer(Modifier.width(4.dp))
            FilledIconButton(
                modifier = Modifier.width(64.dp),
                onClick = { onPlaySongsClick(songs, DEFAULT_INDEX) },
            ) {
                Icon(
                    imageVector = MuzIcons.Rounded.PlayArrow,
                    contentDescription = stringResource(localesR.string.play_audio),
                )
            }
            Spacer(Modifier.width(if (expanded) 2.dp else 4.dp))
        },
        trailingContent = {
            val shuffleButtonLabel = stringResource(localesR.string.shuffle)
            AppBarRow(
                maxItemCount = 1,
                overflowIndicator = { menuState ->
                    IconButton(
                        onClick = {
                            if (menuState.isExpanded) {
                                menuState.dismiss()
                            } else {
                                menuState.show()
                            }
                        },
                    ) {
                        Icon(
                            imageVector = MuzIcons.Rounded.MoreVert,
                            contentDescription = stringResource(localesR.string.open_menu),
                        )
                    }
                },
            ) {
                clickableItem(
                    onClick = { onShuffleSongsClick(songs, DEFAULT_INDEX) },
                    icon = {
                        Icon(
                            imageVector = MuzIcons.Rounded.Shuffle,
                            contentDescription = stringResource(localesR.string.shuffle),
                        )
                    },
                    label = shuffleButtonLabel,
                )
            }
        },
    )
}