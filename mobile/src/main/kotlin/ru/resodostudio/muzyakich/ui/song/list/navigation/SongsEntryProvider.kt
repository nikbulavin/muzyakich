package ru.resodostudio.muzyakich.ui.song.list.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.ui.song.list.SongsScreen

fun EntryProviderScope<NavKey>.songsEntry() {
    entry<SongsNavKey> {
        SongsScreen()
    }
}