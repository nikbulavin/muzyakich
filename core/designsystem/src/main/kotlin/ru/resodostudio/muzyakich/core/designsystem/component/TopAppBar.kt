package ru.resodostudio.muzyakich.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuzTopAppBar(
    @StringRes titleRes: Int,
    navigationIcon: ImageVector,
    @StringRes navigationIconContentDescriptionRes: Int,
    actionIcon: ImageVector,
    @StringRes actionIconContentDescriptionRes: Int,
    modifier: Modifier = Modifier,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(titleRes)) },
        modifier = modifier.testTag("muzTopAppBar"),
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = stringResource(navigationIconContentDescriptionRes),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = stringResource(actionIconContentDescriptionRes),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
    )
}