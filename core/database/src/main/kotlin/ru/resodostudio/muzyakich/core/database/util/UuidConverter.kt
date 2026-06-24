package ru.resodostudio.muzyakich.core.database.util

import androidx.room3.ColumnTypeConverter
import kotlin.uuid.Uuid

internal class UuidConverter {

    @ColumnTypeConverter
    fun uuidToString(value: Uuid?): String? = value?.toHexString()

    @ColumnTypeConverter
    fun stringToUuid(value: String?): Uuid? = value?.let(Uuid::parseHexOrNull)
}