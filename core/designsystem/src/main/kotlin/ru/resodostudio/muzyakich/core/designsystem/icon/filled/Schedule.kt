package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.Schedule: ImageVector
    get() {
        if (_Schedule != null) {
            return _Schedule!!
        }
        _Schedule = ImageVector.Builder(
            name = "Filled.Schedule",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(520f, 464f)
                lineTo(520f, 320f)
                quadTo(520f, 303f, 508.5f, 291.5f)
                quadTo(497f, 280f, 480f, 280f)
                quadTo(463f, 280f, 451.5f, 291.5f)
                quadTo(440f, 303f, 440f, 320f)
                lineTo(440f, 479f)
                quadTo(440f, 487f, 443f, 494.5f)
                quadTo(446f, 502f, 452f, 508f)
                lineTo(584f, 640f)
                quadTo(595f, 651f, 612f, 651f)
                quadTo(629f, 651f, 640f, 640f)
                quadTo(651f, 629f, 651f, 612f)
                quadTo(651f, 595f, 640f, 584f)
                lineTo(520f, 464f)
                close()
                moveTo(480f, 880f)
                quadTo(397f, 880f, 324f, 848.5f)
                quadTo(251f, 817f, 197f, 763f)
                quadTo(143f, 709f, 111.5f, 636f)
                quadTo(80f, 563f, 80f, 480f)
                quadTo(80f, 397f, 111.5f, 324f)
                quadTo(143f, 251f, 197f, 197f)
                quadTo(251f, 143f, 324f, 111.5f)
                quadTo(397f, 80f, 480f, 80f)
                quadTo(563f, 80f, 636f, 111.5f)
                quadTo(709f, 143f, 763f, 197f)
                quadTo(817f, 251f, 848.5f, 324f)
                quadTo(880f, 397f, 880f, 480f)
                quadTo(880f, 563f, 848.5f, 636f)
                quadTo(817f, 709f, 763f, 763f)
                quadTo(709f, 817f, 636f, 848.5f)
                quadTo(563f, 880f, 480f, 880f)
                close()
            }
        }.build()

        return _Schedule!!
    }

@Suppress("ObjectPropertyName")
private var _Schedule: ImageVector? = null
