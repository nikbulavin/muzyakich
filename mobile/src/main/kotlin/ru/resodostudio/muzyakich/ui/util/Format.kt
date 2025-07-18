package ru.resodostudio.muzyakich.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.util.Locale
import ru.resodostudio.muzyakich.core.locales.R as localesR

fun Long.asFormattedString(): String {
    val minutes = (this / 60)
    val seconds = (this % 60)
    return "%d:%02d".format(Locale.getDefault(), minutes, seconds)
}

@Composable
fun Int.toMegabytesString(): String {
    val megabytes = this.toFloat() / 1024 / 1024
    return stringResource(localesR.string.megabytes_format, megabytes)
}

fun convertToProgress(count: Long, total: Long): Float {
    return (count * 100f / total / 100f).takeIf(Float::isFinite) ?: 0f
}

fun convertToPosition(value: Float, total: Long) = (value * total).toLong()