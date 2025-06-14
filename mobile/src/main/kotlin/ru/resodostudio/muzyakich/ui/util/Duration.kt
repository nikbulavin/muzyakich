package ru.resodostudio.muzyakich.ui.util

import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

fun Long.asFormattedString() = milliseconds.toComponents { minutes, seconds, _ ->
    String.format(locale = Locale.US, format = "%d:%02d", minutes, seconds)
}

fun convertToProgress(count: Long, total: Long): Float {
    return (count * 100f / total / 100f).takeIf(Float::isFinite) ?: 0f
}

fun convertToPosition(value: Float, total: Long) = (value * total).toLong()