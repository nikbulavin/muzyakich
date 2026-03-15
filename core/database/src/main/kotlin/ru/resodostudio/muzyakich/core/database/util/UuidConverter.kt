package ru.resodostudio.muzyakich.core.database.util

import androidx.room.TypeConverter
import kotlin.uuid.Uuid

internal class UuidConverter {

    @TypeConverter
    fun uuidToString(value: Uuid?): String? = value?.toHexString()

    @TypeConverter
    fun stringToUuid(value: String?): Uuid? = value?.let(Uuid::parseHexOrNull)
}