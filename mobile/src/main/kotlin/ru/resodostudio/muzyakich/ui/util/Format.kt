package ru.resodostudio.muzyakich.ui.util

import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLocale
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

@Composable
fun Long.asFormattedDuration(): String {
    val hours = TimeUnit.MILLISECONDS.toHours(this)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) % 60

    val measureFormat = MeasureFormat.getInstance(
        LocalLocale.current.platformLocale,
        MeasureFormat.FormatWidth.WIDE,
    )

    return if (hours > 0) {
        measureFormat.formatMeasures(
            Measure(hours, MeasureUnit.HOUR),
            Measure(minutes, MeasureUnit.MINUTE),
        )
    } else {
        measureFormat.formatMeasures(
            Measure(minutes, MeasureUnit.MINUTE),
        )
    }
}
