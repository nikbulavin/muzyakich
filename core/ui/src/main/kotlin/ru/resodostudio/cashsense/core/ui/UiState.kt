package ru.resodostudio.cashsense.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingState(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.testTag("loadingCircle"),
        contentAlignment = Alignment.Center,
        content = { LoadingIndicator() },
    )
}

@Composable
fun EmptyState(
    @StringRes messageRes: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(messageRes),
            maxLines = 4,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}