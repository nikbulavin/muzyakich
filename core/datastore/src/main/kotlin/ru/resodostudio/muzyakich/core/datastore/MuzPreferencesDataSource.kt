package ru.resodostudio.muzyakich.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig.DARK
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig.FOLLOW_SYSTEM
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig.LIGHT
import ru.resodostudio.muzyakich.core.model.data.UserData
import javax.inject.Inject

class MuzPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
        .catch {
            Log.e(TAG, "Failed to read user preferences.", it)
            emit(getCustomInstance())
        }
        .map {
            UserData(
                darkThemeConfig = when (it.darkThemeConfig) {
                    null,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                    DarkThemeConfigProto.UNRECOGNIZED,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
                        -> FOLLOW_SYSTEM

                    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT -> LIGHT
                    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DARK
                },
                useDynamicColor = it.useDynamicColor,
                shuffleModeEnabled = it.shuffleModeEnabled,
            )
        }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        runCatching {
            userPreferences.updateData {
                it.copy { this.useDynamicColor = useDynamicColor }
            }
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        runCatching {
            userPreferences.updateData {
                it.copy {
                    this.darkThemeConfig = when (darkThemeConfig) {
                        FOLLOW_SYSTEM -> DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM
                        LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                        DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                    }
                }
            }
        }
    }

    suspend fun setShuffleModePreference(shuffleModeEnabled: Boolean) {
        runCatching {
            userPreferences.updateData {
                it.copy { this.shuffleModeEnabled = shuffleModeEnabled }
            }
        }
    }
}

private const val TAG = "MuzPreferencesDataSource"