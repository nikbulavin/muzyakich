package ru.resodostudio.muzyakich.core.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Delete
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmationDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    confirmButtonText: String = stringResource(localesR.string.core_locales_delete),
    dismissButtonText: String = stringResource(localesR.string.core_locales_cancel),
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                imageVector = MuzIcons.Filled.Delete,
                contentDescription = null,
            )
        },
        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
            )
        },
        text = {
            Text(text)
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismissRequest()
                    onConfirm()
                },
                shapes = ButtonDefaults.shapes(),
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                shapes = ButtonDefaults.shapes(),
            ) {
                Text(dismissButtonText)
            }
        },
        modifier = modifier,
    )
}