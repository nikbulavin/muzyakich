package ru.resodostudio.muzyakich.ui.songs.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.ui.songs.SongsScreen

fun EntryProviderScope<NavKey>.songsEntry() {
    entry<SongsNavKey> {
        SongsScreen()
    }
}