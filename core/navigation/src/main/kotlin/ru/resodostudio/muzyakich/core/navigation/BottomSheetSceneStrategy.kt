package ru.resodostudio.muzyakich.core.navigation

import android.net.Uri
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue.Expanded
import androidx.compose.material3.SheetValue.Hidden
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import ru.resodostudio.muzyakich.core.ui.util.DynamicPlayerTheme

@OptIn(ExperimentalMaterial3Api::class)
internal data class BottomSheetScene<T : Any>(
    override val key: T,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val entry: NavEntry<T>,
    private val modalBottomSheetProperties: ModalBottomSheetProperties,
    private val contentWindowInsets: WindowInsets?,
    private val artworkUri: State<Uri?>?,
    private val isDarkTheme: Boolean,
    private val onBack: () -> Unit,
) : OverlayScene<T> {

    override val entries: List<NavEntry<T>> = listOf(entry)

    override val content: @Composable (() -> Unit) = {
        val sheetState = rememberBottomSheetState(
            initialValue = Hidden,
            enabledValues = setOf(Hidden, Expanded),
        )
        val lifecycleOwner = rememberLifecycleOwner()

        DynamicPlayerTheme(
            artworkUri = artworkUri?.value,
            isDarkTheme = isDarkTheme,
        ) {
            ModalBottomSheet(
                onDismissRequest = onBack,
                sheetState = sheetState,
                properties = modalBottomSheetProperties,
                contentWindowInsets = { contentWindowInsets ?: BottomSheetDefaults.modalWindowInsets },
            ) {
                CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
                    entry.Content()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null
        val bottomSheetProperties = lastEntry.metadata[BottomSheetKey] ?: return null
        val contentWindowInsets = lastEntry.metadata[BottomSheetInsetsKey]

        val artworkUri = lastEntry.metadata[BottomSheetArtworkUriKey]
        val isDarkTheme = lastEntry.metadata[BottomSheetIsDarkThemeKey] ?: false

        return bottomSheetProperties.let { properties ->
            @Suppress("UNCHECKED_CAST")
            BottomSheetScene(
                key = lastEntry.contentKey as T,
                previousEntries = entries.dropLast(1),
                overlaidEntries = entries.dropLast(1),
                entry = lastEntry,
                modalBottomSheetProperties = properties,
                contentWindowInsets = contentWindowInsets,
                artworkUri = artworkUri,
                isDarkTheme = isDarkTheme,
                onBack = onBack,
            )
        }
    }

    companion object {
        fun bottomSheet(
            modalBottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
            contentWindowInsets: WindowInsets? = null,
            artworkUri: State<Uri?>? = null,
            isDarkTheme: Boolean = false,
        ): Map<String, Any> {
            return metadata {
                put(BottomSheetKey, modalBottomSheetProperties)
                if (contentWindowInsets != null) put(BottomSheetInsetsKey, contentWindowInsets)
                if (artworkUri != null) put(BottomSheetArtworkUriKey, artworkUri)
                put(BottomSheetIsDarkThemeKey, isDarkTheme)
            }
        }

        object BottomSheetKey : NavMetadataKey<ModalBottomSheetProperties>
        object BottomSheetInsetsKey : NavMetadataKey<WindowInsets>
        object BottomSheetArtworkUriKey : NavMetadataKey<State<Uri?>>
        object BottomSheetIsDarkThemeKey : NavMetadataKey<Boolean>
    }
}