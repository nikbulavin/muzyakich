package ru.resodostudio.muzyakich.core.mediastore.util

import android.database.Cursor

internal fun Cursor.getLong(columnName: String) = getLong(getColumnIndexOrThrow(columnName))
internal fun Cursor.getInt(columnName: String) = getInt(getColumnIndexOrThrow(columnName))
internal fun Cursor.getString(columnName: String) = getString(getColumnIndexOrThrow(columnName))
