package ru.resodostudio.muzyakich.feature.playlist.editor.impl

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledIconButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Delete
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Edit
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowBack
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Check
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun PlaylistEditorScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaylistEditorViewModel = hiltViewModel(),
) {
    val playlistEditorUiState by viewModel.playlistEditorUiState.collectAsStateWithLifecycle()

    PlaylistEditorScreen(
        playlistEditorUiState = playlistEditorUiState,
        onBackClick = onBackClick,
        onNameChange = viewModel::onNameChange,
        onCoverSelected = viewModel::updateCover,
        onRemoveCover = viewModel::removeCover,
        onSave = {
            viewModel.savePlaylist()
            onBackClick()
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PlaylistEditorScreen(
    playlistEditorUiState: PlaylistEditorUiState,
    onBackClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onCoverSelected: (Uri?) -> Unit,
    onRemoveCover: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = onCoverSelected,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    MuzIconButton(
                        icon = MuzIcons.Rounded.ArrowBack,
                        onClick = onBackClick,
                        contentDescription = stringResource(localesR.string.core_locales_back),
                        tooltipPosition = TooltipAnchorPosition.Right,
                    )
                },
                actions = {
                    MuzFilledIconButton(
                        icon = MuzIcons.Rounded.Check,
                        onClick = onSave,
                        contentDescription = stringResource(localesR.string.core_locales_confirm),
                        tooltipPosition = TooltipAnchorPosition.Left,
                        enabled = playlistEditorUiState is PlaylistEditorUiState.Success && playlistEditorUiState.name.isNotBlank(),
                        containerSize = IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide),
                        modifier = Modifier.padding(end = 6.dp),
                    )
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    if (playlistEditorUiState is PlaylistEditorUiState.Success) {
                        val coverModel = playlistEditorUiState.selectedCoverUri ?: playlistEditorUiState.coverFileName
                        if (coverModel != null) {
                            AsyncImage(
                                model = coverModel,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp),
                        ) {
                            if (coverModel != null) {
                                MuzFilledIconButton(
                                    icon = MuzIcons.Filled.Delete,
                                    onClick = onRemoveCover,
                                    contentDescription = stringResource(localesR.string.core_locales_remove_cover),
                                )
                            }
                            MuzFilledIconButton(
                                icon = MuzIcons.Filled.Edit,
                                onClick = {
                                    runCatching {
                                        pickMedia.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                                        )
                                    }
                                },
                                contentDescription = stringResource(localesR.string.core_locales_set_cover),
                            )
                        }
                    }
                }
            }
            item {
                if (playlistEditorUiState is PlaylistEditorUiState.Success) {
                    OutlinedTextField(
                        value = playlistEditorUiState.name,
                        onValueChange = onNameChange,
                        label = { Text(stringResource(localesR.string.core_locales_title)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        singleLine = true,
                    )
                }
            }
        }
    }
}
