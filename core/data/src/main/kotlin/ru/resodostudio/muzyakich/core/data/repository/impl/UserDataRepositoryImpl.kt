package ru.resodostudio.muzyakich.core.data.repository.impl

import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.data.repository.UserDataRepository
import ru.resodostudio.muzyakich.core.datastore.MuzPreferencesDataSource
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig
import ru.resodostudio.muzyakich.core.model.data.RepeatMode
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import ru.resodostudio.muzyakich.core.model.data.UserData
import javax.inject.Inject

internal class UserDataRepositoryImpl @Inject constructor(
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

    override suspend fun setRepeatModePreference(repeatMode: RepeatMode) {
        return muzPreferencesDataSource.setRepeatModePreference(repeatMode)
    }

    override suspend fun setSortByPreference(sortBy: SortBy) {
        return muzPreferencesDataSource.setSortByPreference(sortBy)
    }

    override suspend fun setSortOrderPreference(sortOrder: SortOrder) {
        return muzPreferencesDataSource.setSortOrderPreference(sortOrder)
    }
}