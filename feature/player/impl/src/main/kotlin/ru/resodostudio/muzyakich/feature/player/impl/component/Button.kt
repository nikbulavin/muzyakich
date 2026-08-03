package ru.resodostudio.muzyakich.feature.player.impl.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonDefaults.smallContainerSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledTonalIconButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledTonalIconToggleButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Star
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MoreVert
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Star
import ru.resodostudio.muzyakich.core.model.Song
import ru.resodostudio.muzyakich.core.locales.R as localesR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MoreIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MuzFilledTonalIconButton(
        modifier = modifier,
        onClick = onClick,
        containerSize = smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Narrow),
        icon = MuzIcons.Rounded.MoreVert,
        contentDescription = stringResource(localesR.string.core_locales_open_menu),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoriteToggleButton(
    song: Song,
    onFavoriteChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (icon, contentDescription) = if (song.isFavorite) {
        MuzIcons.Filled.Star to stringResource(localesR.string.core_locales_remove_from_favorites)
    } else {
        MuzIcons.Rounded.Star to stringResource(localesR.string.core_locales_add_to_favorites)
    }
    MuzFilledTonalIconToggleButton(
        checked = song.isFavorite,
        onCheckedChange = { onFavoriteChange(song.mediaId, it) },
        modifier = modifier,
        icon = icon,
        contentDescription = contentDescription,
    )
}