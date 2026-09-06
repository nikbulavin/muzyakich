package ru.resodostudio.muzyakich.feature.song.editor.impl

import android.app.Activity.RESULT_OK
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import ru.resodostudio.muzyakich.core.designsystem.component.MuzFilledTonalIconButton
import ru.resodostudio.muzyakich.core.designsystem.component.MuzIconButton
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Delete
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Image
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.ArrowBack
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.Check
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.MusicNote
import ru.resodostudio.muzyakich.core.ui.LoadingState
import ru.resodostudio.muzyakich.core.locales.R as localesR

@Composable
internal fun SongEditorScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SongEditorViewModel = hiltViewModel(),
) {
    val songEditorUiState by viewModel.songEditorUiState.collectAsStateWithLifecycle()

    SongEditorScreen(
        songEditorUiState = songEditorUiState,
        onBackClick = onBackClick,
        onTitleChange = viewModel::onTitleChange,
        onArtistChange = viewModel::onArtistChange,
        onAlbumChange = viewModel::onAlbumChange,
        onAlbumArtistChange = viewModel::onAlbumArtistChange,
        onYearChange = viewModel::onYearChange,
        onGenreChange = viewModel::onGenreChange,
        onTrackNumberChange = viewModel::onTrackNumberChange,
        onDiscNumberChange = viewModel::onDiscNumberChange,
        onCommentChange = viewModel::onCommentChange,
        onCoverSelected = viewModel::updateCover,
        onRemoveCover = viewModel::removeCover,
        onSave = {
            viewModel.saveTags(onSuccess = onBackClick)
        },
        onWritePermissionHandled = viewModel::onWritePermissionHandled,
        modifier = modifier,
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
private fun SongEditorScreen(
    songEditorUiState: SongEditorUiState,
    onBackClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onArtistChange: (String) -> Unit,
    onAlbumChange: (String) -> Unit,
    onAlbumArtistChange: (String) -> Unit,
    onYearChange: (String) -> Unit,
    onGenreChange: (String) -> Unit,
    onTrackNumberChange: (String) -> Unit,
    onDiscNumberChange: (String) -> Unit,
    onCommentChange: (String) -> Unit,
    onCoverSelected: (Uri?) -> Unit,
    onRemoveCover: () -> Unit,
    onSave: () -> Unit,
    onWritePermissionHandled: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val intentSenderLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            onSave()
        }
        onWritePermissionHandled()
    }

    if (songEditorUiState is SongEditorUiState.Success) {
        val pendingIntent = songEditorUiState.pendingWriteIntent
        LaunchedEffect(pendingIntent) {
            if (pendingIntent != null) {
                intentSenderLauncher.launch(
                    IntentSenderRequest.Builder(pendingIntent.intentSender).build(),
                )
            }
        }

        val errorMsg = songEditorUiState.errorMsg
        LaunchedEffect(errorMsg) {
            if (errorMsg != null) {
                snackbarHostState.showSnackbar(errorMsg)
            }
        }
    }

    val isSaveEnabled = songEditorUiState is SongEditorUiState.Success &&
            !songEditorUiState.isSaving &&
            songEditorUiState.title.isNotBlank()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(localesR.string.core_locales_edit_tags)) },
                navigationIcon = {
                    MuzIconButton(
                        icon = MuzIcons.Rounded.ArrowBack,
                        onClick = onBackClick,
                        contentDescription = stringResource(localesR.string.core_locales_back),
                        tooltipPosition = TooltipAnchorPosition.Right,
                    )
                },
                actions = {
                    MuzIconButton(
                        icon = MuzIcons.Rounded.Check,
                        onClick = onSave,
                        enabled = isSaveEnabled,
                        contentDescription = stringResource(localesR.string.core_locales_save),
                    )
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        when (songEditorUiState) {
            SongEditorUiState.Loading -> {
                LoadingState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                )
            }

            SongEditorUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = stringResource(localesR.string.core_locales_library_empty))
                }
            }

            is SongEditorUiState.Success -> {
                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    SongArtwork(
                        coverModel = songEditorUiState.coverModel,
                        onCoverSelected = onCoverSelected,
                        onRemoveCover = onRemoveCover,
                    )

                    TextField(
                        value = songEditorUiState.title,
                        onValueChange = onTitleChange,
                        label = { Text(stringResource(localesR.string.core_locales_title)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = TextFieldDefaults.roundedShape,
                        colors = TextFieldDefaults.tonalColors(),
                    )

                    TextField(
                        value = songEditorUiState.artist,
                        onValueChange = onArtistChange,
                        label = { Text(stringResource(localesR.string.core_locales_artist)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = TextFieldDefaults.roundedShape,
                        colors = TextFieldDefaults.tonalColors(),
                    )

                    TextField(
                        value = songEditorUiState.album,
                        onValueChange = onAlbumChange,
                        label = { Text(stringResource(localesR.string.core_locales_albums)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = TextFieldDefaults.roundedShape,
                        colors = TextFieldDefaults.tonalColors(),
                    )

                    TextField(
                        value = songEditorUiState.albumArtist,
                        onValueChange = onAlbumArtistChange,
                        label = { Text(stringResource(localesR.string.core_locales_album_artist)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = TextFieldDefaults.roundedShape,
                        colors = TextFieldDefaults.tonalColors(),
                    )

                    TextField(
                        value = songEditorUiState.year,
                        onValueChange = onYearChange,
                        label = { Text(stringResource(localesR.string.core_locales_year)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = TextFieldDefaults.roundedShape,
                        colors = TextFieldDefaults.tonalColors(),
                    )

                    TextField(
                        value = songEditorUiState.genre,
                        onValueChange = onGenreChange,
                        label = { Text(stringResource(localesR.string.core_locales_genre)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = TextFieldDefaults.roundedShape,
                        colors = TextFieldDefaults.tonalColors(),
                    )

                    TextField(
                        value = songEditorUiState.trackNumber,
                        onValueChange = onTrackNumberChange,
                        label = { Text(stringResource(localesR.string.core_locales_track_number)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = TextFieldDefaults.roundedShape,
                        colors = TextFieldDefaults.tonalColors(),
                    )

                    TextField(
                        value = songEditorUiState.discNumber,
                        onValueChange = onDiscNumberChange,
                        label = { Text(stringResource(localesR.string.core_locales_disc_number_label)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = TextFieldDefaults.roundedShape,
                        colors = TextFieldDefaults.tonalColors(),
                    )

                    TextField(
                        value = songEditorUiState.comment,
                        onValueChange = onCommentChange,
                        label = { Text(stringResource(localesR.string.core_locales_comment)) },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth(),
                        shape = TextFieldDefaults.roundedShape,
                        colors = TextFieldDefaults.tonalColors(),
                    )
                }
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
private fun SongArtwork(
    coverModel: Any?,
    onCoverSelected: (Uri?) -> Unit,
    onRemoveCover: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = onCoverSelected,
    )

    Box(
        modifier = modifier,
    ) {
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
                        imageVector = MuzIcons.Rounded.MusicNote,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(0.35f),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MuzFilledTonalIconButton(
                icon = MuzIcons.Filled.Image,
                onClick = {
                    runCatching {
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                        )
                    }
                },
                contentDescription = stringResource(localesR.string.core_locales_set_cover),
            )

            AnimatedVisibility(
                visible = coverModel != null,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                MuzFilledTonalIconButton(
                    icon = MuzIcons.Filled.Delete,
                    onClick = onRemoveCover,
                    contentDescription = stringResource(localesR.string.core_locales_remove_cover),
                )
            }
        }
    }
}
