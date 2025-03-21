package ru.resodostudio.muzyakich.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
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
            )
        }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        try {
            userPreferences.updateData {
                it.copy { this.useDynamicColor = useDynamicColor }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to update dynamic color.", e)
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.darkThemeConfig = when (darkThemeConfig) {
                        FOLLOW_SYSTEM -> DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM
                        LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                        DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                    }
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to update theme config.", e)
        }
    }
}

private const val TAG = "MuzPreferencesDataSource"