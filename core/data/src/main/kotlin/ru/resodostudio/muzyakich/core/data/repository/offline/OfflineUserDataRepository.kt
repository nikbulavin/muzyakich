package ru.resodostudio.muzyakich.core.data.repository.offline

import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.data.repository.UserDataRepository
import ru.resodostudio.muzyakich.core.datastore.MuzPreferencesDataSource
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig
import ru.resodostudio.muzyakich.core.model.data.UserData
import javax.inject.Inject

internal class OfflineUserDataRepository @Inject constructor(
    private val muzPreferencesDataSource: MuzPreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> = muzPreferencesDataSource.userData

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        return muzPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        return muzPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
    }

    override suspend fun setShuffleModePreference(shuffleModeEnabled: Boolean) {
        return muzPreferencesDataSource.setShuffleModePreference(shuffleModeEnabled)
    }
}