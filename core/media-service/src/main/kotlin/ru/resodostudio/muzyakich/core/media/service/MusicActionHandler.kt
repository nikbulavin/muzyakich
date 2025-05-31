package ru.resodostudio.muzyakich.core.media.service

import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.common.Constants.PLAYBACK_MODE_REPEAT
import ru.resodostudio.muzyakich.core.common.Constants.PLAYBACK_MODE_REPEAT_ONE
import ru.resodostudio.muzyakich.core.common.Constants.PLAYBACK_MODE_SHUFFLE
import ru.resodostudio.muzyakich.core.common.di.ApplicationScope
import javax.inject.Inject

class MusicActionHandler @Inject constructor(
    @ApplicationScope private val appScope: CoroutineScope,
) {
    val customCommands = getAvailableCustomCommands()
    private val customLayoutMap = mutableMapOf<String, CommandButton>()
    val customLayout: List<CommandButton> get() = customLayoutMap.values.toList()

    fun onCustomCommand(mediaSession: MediaSession, customCommand: SessionCommand) {
        when (customCommand.customAction) {
            PLAYBACK_MODE_REPEAT, PLAYBACK_MODE_REPEAT_ONE, PLAYBACK_MODE_SHUFFLE -> {
                handleRepeatShuffleCommand(action = customCommand.customAction)
            }
        }
    }

    fun setRepeatShuffleCommand(action: String) {

    }

    private fun handleRepeatShuffleCommand(action: String) = appScope.launch {
        when (action) {
            PLAYBACK_MODE_REPEAT -> {}
            PLAYBACK_MODE_REPEAT_ONE -> {}
            PLAYBACK_MODE_SHUFFLE -> {}
        }
    }

    private fun getAvailableCustomCommands() {

    }
}