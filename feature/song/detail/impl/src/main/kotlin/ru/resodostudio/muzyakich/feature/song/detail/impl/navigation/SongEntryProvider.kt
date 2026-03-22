package ru.resodostudio.muzyakich.feature.song.detail.impl.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.resodostudio.muzyakich.core.navigation.BottomSheetSceneStrategy
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.feature.song.detail.api.SongNavKey
import ru.resodostudio.muzyakich.feature.song.detail.impl.SongBottomSheet
import ru.resodostudio.muzyakich.feature.song.detail.impl.SongViewModel

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.songEntry(navigator: Navigator) {
    entry<SongNavKey>(
        metadata = BottomSheetSceneStrategy.bottomSheet(),
    ) { key ->
        SongBottomSheet(
            onDismiss = navigator::goBack,
            viewModel = hiltViewModel<SongViewModel, SongViewModel.Factory> {
                it.create(key.mediaId)
            },
        )
    }
}