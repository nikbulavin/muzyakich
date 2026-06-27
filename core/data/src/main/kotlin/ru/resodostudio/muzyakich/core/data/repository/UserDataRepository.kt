package ru.resodostudio.muzyakich.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.resodostudio.muzyakich.core.model.DarkThemeConfig
import ru.resodostudio.muzyakich.core.model.SortBy
import ru.resodostudio.muzyakich.core.model.SortOrder
import ru.resodostudio.muzyakich.core.model.UserData

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    suspend fun setSortByPreference(sortBy: SortBy)

    suspend fun setSortOrderPreference(sortOrder: SortOrder)

    suspend fun setFilterFavoritesPreference(shouldFilterFavorites: Boolean)
}