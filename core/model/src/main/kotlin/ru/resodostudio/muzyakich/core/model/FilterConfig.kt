package ru.resodostudio.muzyakich.core.model

data class FilterConfig(
    val sortBy: SortBy,
    val sortOrder: SortOrder,
    val shouldFilterFavorites: Boolean,
)
