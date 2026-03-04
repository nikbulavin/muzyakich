package ru.resodostudio.muzyakich.ui.song.detail.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.BottomSheetSceneStrategy

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.songEntry() {
    entry<SongNavKey>(
        metadata = BottomSheetSceneStrategy.bottomSheet(),
    ) { key ->

    }
}