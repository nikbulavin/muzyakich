package ru.resodostudio.muzyakich.feature.song.list.impl.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import ru.resodostudio.muzyakich.feature.song.list.impl.SongsScreen

@Composable
fun SongsEntry(
    onSongMenuClick: (String) -> Unit,
    innerPadding: PaddingValues,
) {
    SongsScreen(
        onSongMenuClick = onSongMenuClick,
        innerPadding = innerPadding,
    )
}