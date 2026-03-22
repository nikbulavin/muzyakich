package ru.resodostudio.muzyakich.ui.song.list.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.feature.song.list.api.SongsNavKey
import ru.resodostudio.muzyakich.ui.song.list.SongsScreen

fun EntryProviderScope<NavKey>.songsEntry(onSongMenuClick: (String) -> Unit) {
    entry<SongsNavKey> {
        SongsScreen(
            onSongMenuClick = onSongMenuClick,
        )
    }
}