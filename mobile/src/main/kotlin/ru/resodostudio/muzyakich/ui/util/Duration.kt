package ru.resodostudio.muzyakich.ui.util

import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

fun Long.asFormattedString() = milliseconds.toComponents { minutes, seconds, _ ->
    val displayMinutes = String.format(locale = Locale.US, format = "%d", minutes)
    val displaySeconds = String.format(locale = Locale.US, format = "%02d", seconds)

    "$displayMinutes:$displaySeconds"
}

fun convertToProgress(count: Long, total: Long): Float {
    return (count * 100f / total / 100f).takeIf(Float::isFinite) ?: 0f
}

fun convertToPosition(value: Float, total: Long) = (value * total).toLong()