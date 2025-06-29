package ru.resodostudio.muzyakich.ui.util

import java.util.Locale

fun Long.asFormattedString(): String {
    val minutes = (this / 60)
    val seconds = (this % 60)
    return String.format(locale = Locale.US, format = "%d:%02d", minutes, seconds)
}

fun convertToProgress(count: Long, total: Long): Float {
    return (count * 100f / total / 100f).takeIf(Float::isFinite) ?: 0f
}

fun convertToPosition(value: Float, total: Long) = (value * total).toLong()