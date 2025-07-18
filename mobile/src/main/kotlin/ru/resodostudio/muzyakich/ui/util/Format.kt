package ru.resodostudio.muzyakich.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.text.NumberFormat
import java.util.Locale
import ru.resodostudio.muzyakich.core.locales.R as localesR

fun Long.asFormattedString(): String {
    val minutes = (this / 60)
    val seconds = (this % 60)
    return "%d:%02d".format(Locale.getDefault(), minutes, seconds)
}

@Composable
fun formatSampleRate(rate: Float, locale: Locale = Locale.getDefault()): String {
    val numberFormat = NumberFormat.getNumberInstance(locale).apply {
        maximumFractionDigits = 1
        minimumFractionDigits = 0
        isGroupingUsed = false
    }
    val formatted = numberFormat.format(rate)
    return stringResource(localesR.string.sample_rate_format, formatted)
}

fun convertToProgress(count: Long, total: Long): Float {
    return (count * 100f / total / 100f).takeIf(Float::isFinite) ?: 0f
}

fun convertToPosition(value: Float, total: Long) = (value * total).toLong()