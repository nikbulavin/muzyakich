package ru.resodostudio.cashsense.core.ui

import android.app.Activity.RESULT_OK
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.Delete
import ru.resodostudio.muzyakich.core.designsystem.icon.filled.PlaylistPlay
import ru.resodostudio.muzyakich.core.designsystem.icon.rounded.PlaylistRemove
import ru.resodostudio.muzyakich.core.model.data.Song

data class SwipeAction(
    val icon: ImageVector,
    val backgroundColor: Color,
    val iconColor: Color,
    val action: () -> Unit,
)

@Composable
fun rememberDeleteSwipeAction(
    song: Song,
    onSongRemove: (String) -> Unit,
): SwipeAction {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
    ) { result ->
        if (result.resultCode == RESULT_OK) onSongRemove(song.mediaId)
    }
    return SwipeAction(
        icon = MuzIcons.Filled.Delete,
        backgroundColor = MaterialTheme.colorScheme.errorContainer,
        iconColor = MaterialTheme.colorScheme.onErrorContainer,
        action = {
            runCatching {
                val pendingIntent = MediaStore.createTrashRequest(
                    context.contentResolver,
                    listOf(song.mediaUri),
                    true,
                )
                launcher.launch(IntentSenderRequest.Builder(pendingIntent.intentSender).build())
            }.onFailure { exception ->
                exception.printStackTrace()
            }
        },
    )
}

@Composable
fun rememberRemoveFromPlaylistSwipeAction(
    song: Song,
    onRemove: (Song) -> Unit,
): SwipeAction {
    return SwipeAction(
        icon = MuzIcons.Rounded.PlaylistRemove,
        backgroundColor = MaterialTheme.colorScheme.errorContainer,
        iconColor = MaterialTheme.colorScheme.onErrorContainer,
        action = { onRemove(song) },
    )
}

@Composable
fun rememberPlaylistPlaySwipeAction(
    song: Song,
    onSwipe: (Song) -> Unit,
): SwipeAction {
    return SwipeAction(
        icon = MuzIcons.Filled.PlaylistPlay,
        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
        iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
        action = { onSwipe(song) },
    )
}
