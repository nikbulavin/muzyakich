package ru.resodostudio.muzyakich.core.mediastore.util

import android.provider.MediaStore
import ru.resodostudio.muzyakich.core.model.data.SortBy
import ru.resodostudio.muzyakich.core.model.data.SortBy.ARTIST
import ru.resodostudio.muzyakich.core.model.data.SortBy.TITLE
import ru.resodostudio.muzyakich.core.model.data.SortOrder
import ru.resodostudio.muzyakich.core.model.data.SortOrder.*

internal fun buildMediaStoreSortOrder(sortBy: SortBy, sortOrder: SortOrder): String {
    val mediaStoreSortBy = when (sortBy) {
        ARTIST -> MediaStore.Audio.Media.ARTIST
        TITLE -> MediaStore.Audio.Media.TITLE
    }
    val mediaStoreSortOrder = when (sortOrder) {
        ASCENDING -> "ASC"
        DESCENDING -> "DESC"
    }
    return "$mediaStoreSortBy $mediaStoreSortOrder"
}
