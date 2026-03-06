package ru.resodostudio.muzyakich.ui.player.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.BottomSheetSceneStrategy
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.ui.player.PlayerScreen
import ru.resodostudio.muzyakich.ui.song.detail.navigation.navigateToSong

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.playerEntry(navigator: Navigator) {
    entry<PlayerNavKey>(
        metadata = BottomSheetSceneStrategy.bottomSheet(),
    ) {
        PlayerScreen(
            onSongMenuClick = navigator::navigateToSong,
        )
    }
}