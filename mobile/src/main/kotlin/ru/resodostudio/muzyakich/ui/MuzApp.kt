package ru.resodostudio.muzyakich.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.PermMedia
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Add
import ru.resodostudio.muzyakich.core.designsystem.theme.LocalSharedTransitionScope
import ru.resodostudio.muzyakich.core.navigation.BottomSheetSceneStrategy
import ru.resodostudio.muzyakich.core.navigation.Navigator
import ru.resodostudio.muzyakich.core.navigation.rememberNavigationState
import ru.resodostudio.muzyakich.core.navigation.toEntries
import ru.resodostudio.muzyakich.feature.album.detail.impl.navigation.albumEntry
import ru.resodostudio.muzyakich.feature.artist.detail.impl.navigation.artistEntry
import ru.resodostudio.muzyakich.feature.library.api.LibraryNavKey
import ru.resodostudio.muzyakich.feature.library.impl.model.LibraryTab
import ru.resodostudio.muzyakich.feature.library.impl.navigation.libraryEntry
import ru.resodostudio.muzyakich.feature.player.api.PlayerNavKey
import ru.resodostudio.muzyakich.feature.player.api.navigateToPlayer
import ru.resodostudio.muzyakich.feature.player.impl.navigation.playerEntry
import ru.resodostudio.muzyakich.feature.playlist.detail.impl.navigation.playlistEntry
import ru.resodostudio.muzyakich.feature.playlist.editor.api.PlaylistEditorNavKey
import ru.resodostudio.muzyakich.feature.playlist.editor.api.navigateToPlaylistEditor
import ru.resodostudio.muzyakich.feature.playlist.editor.impl.navigation.playlistEditorEntry
import ru.resodostudio.muzyakich.feature.settings.impl.navigation.licensesEntry
import ru.resodostudio.muzyakich.feature.settings.impl.navigation.settingsEntry
import ru.resodostudio.muzyakich.feature.song.detail.impl.navigation.songEntry
import ru.resodostudio.muzyakich.ui.component.NowPlayingBar
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalHazeMaterialsApi::class,
    ExperimentalHazeApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
)
@Composable
fun MuzApp(
    appState: MuzAppState,
) {
    val navigator = remember { Navigator(appState.navigationState) }
    val libraryNavigationState = rememberNavigationState(
        initialBackStack = listOf(LibraryTab.entries.first().navKey),
    )
    val libraryNavigator = remember { Navigator(libraryNavigationState) }
    val snackbarHostState = remember { SnackbarHostState() }

    val nowPlayingState by appState.nowPlayingState.collectAsStateWithLifecycle()
    val player = nowPlayingState.player

    val motionScheme = MaterialTheme.motionScheme
    val hazeState = rememberHazeState()

    val isNowPlayingVisible = appState.navigationState.backStack.none {
        it is PlaylistEditorNavKey || it is PlayerNavKey
    } && player?.currentMediaItem != null

    val fadeSpec = motionScheme.defaultEffectsSpec<Float>()

    val permissionState = rememberMuzyakichPermissionState { mutableStateOf(false) }

    val currentLibraryTab =
        LibraryTab.entries.find { it.navKey == libraryNavigator.state.backStack.last() }
            ?: LibraryTab.entries.first()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            )
        },
        floatingActionButton = {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    )
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AnimatedVisibility(
                    visible = isNowPlayingVisible,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    enter = fadeIn(fadeSpec) +
                            scaleIn(motionScheme.defaultSpatialSpec(), 0.85f) +
                            slideInVertically(motionScheme.defaultSpatialSpec()) { it / 2 } +
                            expandHorizontally(
                                animationSpec = motionScheme.defaultSpatialSpec(),
                                expandFrom = Alignment.CenterHorizontally,
                            ),
                    exit = fadeOut(fadeSpec) + slideOutVertically(motionScheme.fastSpatialSpec()) { it / 2 },
                ) {
                    val nowPlayingBarHazeStyle = HazeMaterials.ultraThin(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    )
                    val hazeBlurRadius = 32.dp
                    with(LocalSharedTransitionScope.current) {
                        if (player != null) {
                            NowPlayingBar(
                                player = player,
                                modifier = Modifier
                                    .renderInSharedTransitionScopeOverlay(2f)
                                    .shadow(
                                        elevation = 6.dp,
                                        shape = CircleShape,
                                        clip = true,
                                    )
                                    .hazeEffect(hazeState, nowPlayingBarHazeStyle) {
                                        inputScale = HazeInputScale.Auto
                                        blurEnabled = true
                                        blurRadius = hazeBlurRadius
                                        noiseFactor = 0f
                                    },
                                onClick = dropUnlessResumed { navigator.navigateToPlayer() },
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible = navigator.state.currentKey is LibraryNavKey &&
                            permissionState.status == PermissionStatus.Granted,
                    enter = fadeIn(fadeSpec) +
                            scaleIn(motionScheme.defaultSpatialSpec(), 0.85f) +
                            slideInVertically(motionScheme.defaultSpatialSpec()) { it / 2 },
                    exit = fadeOut(fadeSpec) + slideOutVertically(motionScheme.fastSpatialSpec()) { it / 2 },
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        HorizontalFloatingToolbar(
                            expanded = true,
                            content = {
                                val tabs = LibraryTab.entries
                                val labels = tabs.associateWith { stringResource(it.titleRes) }
                                AppBarRow {
                                    for (tab in tabs) {
                                        clickableItem(
                                            onClick = { libraryNavigator.navigateAndClearStack(tab.navKey) },
                                            icon = {
                                                Icon(
                                                    imageVector = tab.icon,
                                                    contentDescription = null,
                                                )
                                            },
                                            label = labels[tab] ?: "",
                                        )
                                    }
                                }
                            },
                        )
                        FloatingToolbarDefaults.StandardFloatingActionButton(
                            onClick = dropUnlessResumed { navigator.navigateToPlaylistEditor() },
                            modifier = Modifier
                                .animateFloatingActionButton(
                                    visible = currentLibraryTab == LibraryTab.PLAYLISTS,
                                    alignment = Alignment.BottomCenter,
                                )
                                .size(56.dp),
                        ) {
                            Icon(
                                imageVector = MuzIcons.Rounded.Add,
                                contentDescription = stringResource(localesR.string.core_locales_new_playlist),
                            )
                        }
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { padding ->
        Box(
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
            when (permissionState.status) {
                is PermissionStatus.Denied -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = stringResource(localesR.string.core_locales_media_permission_rationale),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        val buttonSize = ButtonDefaults.MediumContainerHeight
                        val buttonContentPadding = ButtonDefaults.contentPaddingFor(
                            buttonHeight = buttonSize,
                            hasStartIcon = true,
                        )
                        Button(
                            shapes = ButtonDefaults.shapes(),
                            onClick = { permissionState.launchPermissionRequest() },
                            modifier = Modifier.heightIn(buttonSize),
                            contentPadding = buttonContentPadding,
                        ) {
                            Icon(
                                imageVector = MuzIcons.Filled.PermMedia,
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.iconSizeFor(buttonSize)),
                            )
                            Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(buttonSize)))
                            Text(
                                text = stringResource(localesR.string.core_locales_grant_permission),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = ButtonDefaults.textStyleFor(buttonSize),
                            )
                        }
                    }
                }

                PermissionStatus.Granted -> {
                    val entryProvider = entryProvider {
                        libraryEntry(
                            navigator = navigator,
                            libraryNavigator = libraryNavigator,
                        )
                        playerEntry(navigator)
                        albumEntry(navigator, fadeSpec)
                        artistEntry(navigator)
                        songEntry(navigator)
                        settingsEntry(navigator)
                        licensesEntry(navigator)
                        playlistEditorEntry(navigator)
                        playlistEntry(navigator, fadeSpec)
                    }

                    NavDisplay(
                        modifier = Modifier.hazeSource(hazeState),
                        entries = appState.navigationState.toEntries(entryProvider),
                        onBack = navigator::goBack,
                        transitionSpec = {
                            slideInHorizontally(motionScheme.defaultSpatialSpec()) { it } togetherWith
                                    slideOutHorizontally(motionScheme.defaultSpatialSpec()) { -it }
                        },
                        popTransitionSpec = {
                            slideInHorizontally(motionScheme.defaultSpatialSpec()) { -it } togetherWith
                                    slideOutHorizontally(motionScheme.defaultSpatialSpec()) { it }
                        },
                        predictivePopTransitionSpec = {
                            slideInHorizontally(motionScheme.defaultSpatialSpec()) { -it } togetherWith
                                    slideOutHorizontally(motionScheme.defaultSpatialSpec()) { it }
                        },
                        sharedTransitionScope = LocalSharedTransitionScope.current,
                        sceneStrategies = listOf(
                            remember { BottomSheetSceneStrategy() },
                        ),
                    )
                }
            }
        }
    }
}