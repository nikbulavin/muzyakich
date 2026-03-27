package ru.resodostudio.muzyakich.feature.playlist.editor.impl

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import ru.resodostudio.cashsense.core.ui.LoadingState
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledIconButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Delete
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Edit
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowBack
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Check
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.LibraryMusic
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.feature.song.picker.SongPickerBottomSheet
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
        onTitleChange = viewModel::onTitleChange,
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
    onTitleChange: (String) -> Unit,
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
                        enabled = playlistEditorUiState is PlaylistEditorUiState.Success && playlistEditorUiState.title.isNotBlank(),
                        containerSize = IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide),
                        modifier = Modifier.padding(end = 6.dp),
                    )
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        when (playlistEditorUiState) {
            PlaylistEditorUiState.Loading -> {
                LoadingState(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                )
            }

            is PlaylistEditorUiState.Success -> {
                LazyColumn(
                    contentPadding = innerPadding + PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item {
                        Box {
                            val coverModel = playlistEditorUiState.selectedCoverUri
                                ?: playlistEditorUiState.coverFilePath
                            SubcomposeAsyncImage(
                                model = coverModel,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(MaterialTheme.shapes.large),
                                error = {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(MaterialTheme.colorScheme.surfaceVariant),
                                    ) {
                                        Icon(
                                            imageVector = MuzIcons.Rounded.LibraryMusic,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(0.35f),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                },
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(16.dp),
                            ) {
                                MuzFilledIconButton(
                                    icon = MuzIcons.Filled.Delete,
                                    onClick = onRemoveCover,
                                    contentDescription = stringResource(localesR.string.core_locales_remove_cover),
                                    enabled = coverModel != null,
                                )
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
                    item {
                        OutlinedTextField(
                            value = playlistEditorUiState.title,
                            onValueChange = onTitleChange,
                            label = { Text(stringResource(localesR.string.core_locales_title)) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                        )
                    }
                    item {
                        var shouldShowSongPicker by remember { mutableStateOf(false) }
                        Button(
                            shapes = ButtonDefaults.shapes(),
                            onClick = { shouldShowSongPicker = true },
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = ButtonDefaults.contentPaddingFor(
                                buttonHeight = ButtonDefaults.MinHeight,
                                hasStartIcon = true,
                            ),
                        ) {
                            Icon(
                                imageVector = MuzIcons.Rounded.MusicNote,
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.iconSizeFor(ButtonDefaults.MinHeight)),
                            )
                            Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(ButtonDefaults.MinHeight)))
                            Text(
                                text = "Add songs",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        if (shouldShowSongPicker) {
                            SongPickerBottomSheet(
                                onDismiss = { shouldShowSongPicker = false },
                                onSongsSelected = {},
                            )
                        }
                    }
                }
            }
        }
    }
}
