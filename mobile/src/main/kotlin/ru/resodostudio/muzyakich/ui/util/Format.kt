package ru.resodostudio.muzyakich.ui.util

import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLocale
import java.util.Locale
import java.util.concurrent.TimeUnit

fun Long.asFormattedString(): String {
    val minutes = (this / 60)
    val seconds = (this % 60)
    return "%d:%02d".format(Locale.getDefault(), minutes, seconds)
}

@Composable
fun Float.asFormattedSampleRate(): String {
    return MeasureFormat
        .getInstance(
            LocalLocale.current.platformLocale,
            MeasureFormat.FormatWidth.SHORT,
        )
        .formatMeasures(
            Measure(this, MeasureUnit.KILOHERTZ),
        )
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
