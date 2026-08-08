package ru.resodostudio.muzyakich.feature.player.impl.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.State
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.BottomSheetSceneStrategy
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.player.api.PlayerNavKey
import ru.resodostudio.muzyakich.feature.player.impl.PlayerScreen
import ru.resodostudio.muzyakich.feature.song.detail.api.navigateToSong

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.playerEntry(
    navigator: Navigator,
    contentWindowInsets: WindowInsets,
    artworkUri: State<String?>?,
) {
    entry<PlayerNavKey>(
        metadata = BottomSheetSceneStrategy.bottomSheet(
            contentWindowInsets = contentWindowInsets,
            artworkUri = artworkUri,
        ),
    ) {
        PlayerScreen(
            onDismiss = navigator::goBack,
            onSongMenuClick = navigator::navigateToSong,
        )
    }
}