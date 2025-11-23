package ru.resodostudio.muzyakich.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import java.text.NumberFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
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

fun Float.toSeekPosition(duration: Long) = (this * duration).toLong()

@Composable
fun Long.asFormattedDuration(): String {
    val hours = TimeUnit.MILLISECONDS.toHours(this)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) % 60

    val parts = buildList {
        if (hours > 0) {
            add(pluralStringResource(localesR.plurals.duration_hours, hours.toInt(), hours.toInt()))
        }
        if (minutes > 0) {
            add(pluralStringResource(localesR.plurals.duration_minutes, minutes.toInt(), minutes.toInt()))
        }
    }

    return parts.joinToString(" ")
}