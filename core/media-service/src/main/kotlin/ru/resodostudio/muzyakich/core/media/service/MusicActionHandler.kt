package ru.resodostudio.muzyakich.core.media.service

import android.content.Context
import android.os.Bundle
import androidx.media3.session.CommandButton
import androidx.media3.session.SessionCommand
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.resodostudio.muzyakich.core.common.Constants.REPEAT_MODE
import ru.resodostudio.muzyakich.core.common.Constants.REPEAT_MODE_ALL
import ru.resodostudio.muzyakich.core.common.Constants.REPEAT_MODE_OFF
import ru.resodostudio.muzyakich.core.common.Constants.REPEAT_MODE_ONE
import ru.resodostudio.muzyakich.core.common.Constants.SHUFFLE_MODE
import ru.resodostudio.muzyakich.core.common.Constants.SHUFFLE_MODE_OFF
import ru.resodostudio.muzyakich.core.common.Constants.SHUFFLE_MODE_ON
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.Main
import ru.resodostudio.muzyakich.core.data.repository.UserDataRepository
import ru.resodostudio.muzyakich.core.model.data.RepeatMode
import javax.inject.Inject
import ru.resodostudio.muzyakich.core.locales.R as localesR

class MusicActionHandler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val customCommands = getAvailableCustomCommands()
    private val customLayoutMap = mutableMapOf<String, CommandButton>().apply {
        this[SHUFFLE_MODE] = customCommands.getValue(SHUFFLE_MODE_OFF)
        this[REPEAT_MODE] = customCommands.getValue(REPEAT_MODE_OFF)
    }
    val customLayout: List<CommandButton> get() = customLayoutMap.values.toList()

    fun onCustomCommand(customCommand: SessionCommand) {
        when (customCommand.customAction) {
            REPEAT_MODE_OFF, REPEAT_MODE_ALL, REPEAT_MODE_ONE -> {
                handleRepeatCommand(action = customCommand.customAction)
            }

            SHUFFLE_MODE_OFF, SHUFFLE_MODE_ON -> {
                handleShuffleCommand(action = customCommand.customAction)
            }
        }
    }

    fun setRepeatCommand(action: String) {
        customLayoutMap[REPEAT_MODE] = customCommands.getValue(action)
    }

    fun setShuffleCommand(action: String) {
        customLayoutMap[SHUFFLE_MODE] = customCommands.getValue(action)
    }

    private fun handleRepeatCommand(action: String) {

    }

    private fun handleShuffleCommand(action: String) {

    }

    private fun getAvailableCustomCommands() = mapOf(
        REPEAT_MODE_OFF to buildCustomCommand(
            action = REPEAT_MODE_ALL,
            displayName = context.getString(localesR.string.enable_repeat_mode_all),
            icon = CommandButton.ICON_REPEAT_OFF,
        ),
        REPEAT_MODE_ALL to buildCustomCommand(
            action = REPEAT_MODE_ONE,
            displayName = context.getString(localesR.string.enable_repeat_mode_one),
            icon = CommandButton.ICON_REPEAT_ALL,
        ),
        REPEAT_MODE_ONE to buildCustomCommand(
            action = REPEAT_MODE_OFF,
            displayName = context.getString(localesR.string.disable_repeat_mode),
            icon = CommandButton.ICON_REPEAT_ONE,
        ),
        SHUFFLE_MODE_OFF to buildCustomCommand(
            action = SHUFFLE_MODE_ON,
            displayName = context.getString(localesR.string.enable_shuffle_mode),
            icon = CommandButton.ICON_SHUFFLE_OFF,
        ),
        SHUFFLE_MODE_ON to buildCustomCommand(
            action = SHUFFLE_MODE_OFF,
            displayName = context.getString(localesR.string.disable_shuffle_mode),
            icon = CommandButton.ICON_SHUFFLE_ON,
        ),
    )
}

private fun buildCustomCommand(
    action: String,
    displayName: String,
    icon: Int,
): CommandButton {
    return CommandButton.Builder(icon)
        .setDisplayName(displayName)
        .setSessionCommand(SessionCommand(action, Bundle.EMPTY))
        .build()
}