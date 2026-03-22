package ru.resodostudio.muzyakich.feature.song.list.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.feature.song.list.api.SongsNavKey
import ru.resodostudio.muzyakich.feature.song.list.impl.SongsScreen

fun EntryProviderScope<NavKey>.songsEntry(onSongMenuClick: (String) -> Unit) {
    entry<SongsNavKey> {
        SongsScreen(
            onSongMenuClick = onSongMenuClick,
        )
    }
}