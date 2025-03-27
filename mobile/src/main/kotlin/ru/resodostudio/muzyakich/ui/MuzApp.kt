package ru.resodostudio.muzyakich.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.resodostudio.muzyakich.R
import ru.resodostudio.muzyakich.core.designsystem.component.MuzTopAppBar
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.Search
import ru.resodostudio.muzyakich.core.designsystem.icon.Settings
import ru.resodostudio.muzyakich.ui.library.LibraryScreen
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
fun MuzApp() {
    Scaffold { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
        ) {
            MuzTopAppBar(
                titleRes = R.string.app_name,
                navigationIcon = MuzIcons.Search,
                navigationIconContentDescriptionRes = localesR.string.search,
                actionIcon = MuzIcons.Settings,
                actionIconContentDescriptionRes = localesR.string.settings,
            )

            LibraryScreen()
        }
    }
}