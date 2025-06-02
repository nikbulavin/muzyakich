package ru.resodostudio.muzyakich.core.media.service

import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.common.Constants.REPEAT_MODE_ALL
import ru.resodostudio.muzyakich.core.common.Constants.REPEAT_MODE_ONE
import ru.resodostudio.muzyakich.core.common.Constants.SHUFFLE_MODE
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.Main
import javax.inject.Inject

class MusicActionHandler @Inject constructor(
    @Dispatcher(Main) mainDispatcher: CoroutineDispatcher,
) {
    private val coroutineScope = CoroutineScope(mainDispatcher + SupervisorJob())

    val customCommands = getAvailableCustomCommands()
    private val customLayoutMap = mutableMapOf<String, CommandButton>()
    val customLayout: List<CommandButton> get() = customLayoutMap.values.toList()

    fun onCustomCommand(mediaSession: MediaSession, customCommand: SessionCommand) {
        when (customCommand.customAction) {
            REPEAT_MODE_ALL, REPEAT_MODE_ONE, SHUFFLE_MODE -> {
                handleRepeatShuffleCommand(action = customCommand.customAction)
            }
        }
    }

    fun setRepeatShuffleCommand(action: String) {

    }

    fun cancelCoroutineScope() = coroutineScope.cancel()

    private fun handleRepeatShuffleCommand(action: String) = coroutineScope.launch {
        when (action) {
            REPEAT_MODE_ALL -> {}
            REPEAT_MODE_ONE -> {}
            SHUFFLE_MODE -> {}
        }
    }

    private fun getAvailableCustomCommands() {

    }
}