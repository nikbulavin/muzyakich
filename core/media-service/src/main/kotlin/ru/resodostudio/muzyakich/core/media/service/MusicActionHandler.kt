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
import ru.resodostudio.muzyakich.core.common.Constants.SHUFFLE_MODE
import ru.resodostudio.muzyakich.core.common.Dispatcher
import ru.resodostudio.muzyakich.core.common.MuzDispatchers.Main
import ru.resodostudio.muzyakich.core.data.repository.UserDataRepository
import ru.resodostudio.muzyakich.core.media.notification.common.MusicCommands.SHUFFLE_MODE_OFF
import ru.resodostudio.muzyakich.core.media.notification.common.MusicCommands.SHUFFLE_MODE_ON
import javax.inject.Inject
import ru.resodostudio.muzyakich.core.locales.R as localesR

class MusicActionHandler @Inject constructor(
    @Dispatcher(Main) mainDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context,
    private val userDataRepository: UserDataRepository,
) {
    private val coroutineScope = CoroutineScope(mainDispatcher + SupervisorJob())

    val customCommands = getAvailableCustomCommands()
    private val customLayoutMap = mutableMapOf<String, CommandButton>().apply {
        this[SHUFFLE_MODE] = customCommands.getValue(SHUFFLE_MODE_OFF)
    }
    val customLayout: List<CommandButton> get() = customLayoutMap.values.toList()

    fun onCustomCommand(customCommand: SessionCommand) {
        when (customCommand.customAction) {
            SHUFFLE_MODE_OFF, SHUFFLE_MODE_ON -> {
                handleShuffleCommand(action = customCommand.customAction)
            }
        }
    }

    fun setShuffleCommand(action: String) {
        customLayoutMap[SHUFFLE_MODE] = customCommands.getValue(action)
    }

    fun cancelCoroutineScope() = coroutineScope.cancel()

    private fun handleShuffleCommand(action: String) = coroutineScope.launch {
        when (action) {
            SHUFFLE_MODE_ON -> userDataRepository.setShuffleModePreference(true)
            SHUFFLE_MODE_OFF -> userDataRepository.setShuffleModePreference(false)
        }
    }

    private fun getAvailableCustomCommands() = mapOf(
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
) = CommandButton.Builder(icon)
    .setDisplayName(displayName)
    .setSessionCommand(SessionCommand(action, Bundle.EMPTY))
    .build()