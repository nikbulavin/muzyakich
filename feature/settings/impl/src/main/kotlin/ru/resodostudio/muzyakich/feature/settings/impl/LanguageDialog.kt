package ru.resodostudio.muzyakich.feature.settings.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.component.MuzSelectableListItem
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Language
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun LanguageDialog(
    language: String,
    availableLanguages: List<Language>,
    onLanguageClick: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var languageState by rememberSaveable { mutableStateOf(language) }

    AlertDialog(
        title = {
            Text(
                text = stringResource(localesR.string.core_locales_language),
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
                    onLanguageClick(languageState)
                    onDismiss()
                },
                shapes = ButtonDefaults.shapes(),
            ) {
                Text(stringResource(localesR.string.core_locales_confirm))
            }
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
            ) {
                itemsIndexed(
                    items = availableLanguages,
                    key = { _, language -> language.code },
                ) { index, language ->
                    val selected = language.code == languageState
                    MuzSelectableListItem(
                        leadingContent = {
                            Icon(
                                imageVector = language.icon,
                                contentDescription = null,
                                tint = if (index != 0) Color.Unspecified else LocalContentColor.current,
                                modifier = Modifier.size(24.dp),
                            )
                        },
                        content = {
                            Text(
                                text = language.displayName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        selected = selected,
                        onClick = { languageState = language.code },
                        trailingContent = {
                            RadioButton(
                                selected = selected,
                                onClick = null,
                            )
                        },
                        shapes = ListItemDefaults.segmentedShapes(index, availableLanguages.size),
                        colors = ListItemDefaults.segmentedColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        ),
                    )
                }
            }
        }
    )
}