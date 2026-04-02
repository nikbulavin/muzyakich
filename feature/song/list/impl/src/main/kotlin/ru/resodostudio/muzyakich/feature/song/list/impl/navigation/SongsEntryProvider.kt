package ru.resodostudio.muzyakich.feature.song.list.impl.navigation

import androidx.compose.runtime.Composable
import ru.resodostudio.muzyakich.feature.song.list.impl.SongsScreen

@Composable
fun SongsEntry(onSongMenuClick: (String) -> Unit) {
    SongsScreen(
        onSongMenuClick = onSongMenuClick,
    )
}