package ru.resodostudio.muzyakich.feature.settings.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSelectableListItem
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Language
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ThemeDialog(
    themeConfig: DarkThemeConfig,
    themeOptions: List<Pair<String, ImageVector>>,
    onThemeConfigUpdate: (DarkThemeConfig) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var themeState by rememberSaveable { mutableStateOf(themeConfig) }

    AlertDialog(
        title = {
            Text(
                text = stringResource(localesR.string.core_locales_theme),
                textAlign = TextAlign.Center,
            )
        },
        icon = {
            Icon(
                imageVector = MuzIcons.Rounded.Language,
                contentDescription = null,
            )
        },
        onDismissRequest = onDismiss,
        modifier = modifier,
        confirmButton = {
            Button(
                onClick = {
                    onThemeConfigUpdate(themeState)
                    onDismiss()
                },
                shapes = ButtonDefaults.shapes(),
            ) {
                Text(stringResource(localesR.string.core_locales_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shapes = ButtonDefaults.shapes(),
            ) {
                Text(stringResource(localesR.string.core_locales_cancel))
            }
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
            ) {
                itemsIndexed(
                    items = themeOptions,
                    key = { _, option -> option.first},
                ) { index, (label, icon) ->
                    val selected = themeState.ordinal == index
                    MuzSelectableListItem(
                        content = {
                            Text(
                                text = label,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        selected = selected,
                        onClick = { themeState = DarkThemeConfig.entries[index] },
                        trailingContent = {
                            RadioButton(
                                selected = selected,
                                onClick = null,
                            )
                        },
                        leadingContent = {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                            )
                        },
                        shapes = ListItemDefaults.segmentedShapes(index, themeOptions.size),
                        colors = ListItemDefaults.segmentedColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        ),
                    )
                }
            }
        }
    )
}