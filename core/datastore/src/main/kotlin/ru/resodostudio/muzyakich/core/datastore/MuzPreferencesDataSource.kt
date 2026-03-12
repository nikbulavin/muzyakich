package ru.resodostudio.muzyakich.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig.DARK
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig.FOLLOW_SYSTEM
import ru.resodostudio.muzyakich.core.model.data.DarkThemeConfig.LIGHT
import ru.resodostudio.muzyakich.core.model.data.FilterConfig
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import ru.resodostudio.muzyakich.core.model.data.UserData
import javax.inject.Inject

class MuzPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
        .catch { emit(getCustomInstance()) }
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
                filterConfig = FilterConfig(
                    sortOrder = when (it.sortOrder) {
                        null,
                        SortOrderProto.SORT_ORDER_ASCENDING,
                        SortOrderProto.UNRECOGNIZED,
                            -> SortOrder.ASCENDING

                        SortOrderProto.SORT_ORDER_DESCENDING -> SortOrder.DESCENDING
                    },
                    sortBy = when (it.sortBy) {
                        null,
                        SortByProto.SORT_BY_ARTIST,
                        SortByProto.UNRECOGNIZED,
                            -> SortBy.ARTIST

                        SortByProto.SORT_BY_TITLE -> SortBy.TITLE
                    },
                    shouldFilterFavorites = it.shouldFilterFavorites,
                )
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

    suspend fun setSortByPreference(sortBy: SortBy) {
        runCatching {
            userPreferences.updateData {
                it.copy {
                    this.sortBy = when (sortBy) {
                        SortBy.ARTIST -> SortByProto.SORT_BY_ARTIST
                        SortBy.TITLE -> SortByProto.SORT_BY_TITLE
                    }
                }
            }
        }
    }

    suspend fun setSortOrderPreference(sortOrder: SortOrder) {
        runCatching {
            userPreferences.updateData {
                it.copy {
                    this.sortOrder = when (sortOrder) {
                        SortOrder.ASCENDING -> SortOrderProto.SORT_ORDER_ASCENDING
                        SortOrder.DESCENDING -> SortOrderProto.SORT_ORDER_DESCENDING
                    }
                }
            }
        }
    }

    suspend fun setFilterFavoritesPreference(shouldFilterFavorites: Boolean) {
        runCatching {
            userPreferences.updateData {
                it.copy { this.shouldFilterFavorites = shouldFilterFavorites }
            }
        }
    }
}