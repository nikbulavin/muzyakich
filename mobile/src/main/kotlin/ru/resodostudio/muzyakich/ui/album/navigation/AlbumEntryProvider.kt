package ru.resodostudio.muzyakich.ui.album.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.Navigator

fun EntryProviderScope<NavKey>.albumEntry(navigator: Navigator) {
    entry<AlbumNavKey> { albumKey ->

    }
}