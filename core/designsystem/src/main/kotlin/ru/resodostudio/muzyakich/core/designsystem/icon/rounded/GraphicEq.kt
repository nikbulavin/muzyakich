package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.GraphicEq: ImageVector
    get() {
        if (_GraphicEq != null) {
            return _GraphicEq!!
        }
        _GraphicEq = ImageVector.Builder(
            name = "Rounded.GraphicEq",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(280f, 680f)
                lineTo(280f, 280f)
                quadTo(280f, 263f, 291.5f, 251.5f)
                quadTo(303f, 240f, 320f, 240f)
                quadTo(337f, 240f, 348.5f, 251.5f)
                quadTo(360f, 263f, 360f, 280f)
                lineTo(360f, 680f)
                quadTo(360f, 697f, 348.5f, 708.5f)
                quadTo(337f, 720f, 320f, 720f)
                quadTo(303f, 720f, 291.5f, 708.5f)
                quadTo(280f, 697f, 280f, 680f)
                close()
                moveTo(440f, 840f)
                lineTo(440f, 120f)
                quadTo(440f, 103f, 451.5f, 91.5f)
                quadTo(463f, 80f, 480f, 80f)
                quadTo(497f, 80f, 508.5f, 91.5f)
                quadTo(520f, 103f, 520f, 120f)
                lineTo(520f, 840f)
                quadTo(520f, 857f, 508.5f, 868.5f)
                quadTo(497f, 880f, 480f, 880f)
                quadTo(463f, 880f, 451.5f, 868.5f)
                quadTo(440f, 857f, 440f, 840f)
                close()
                moveTo(120f, 520f)
                lineTo(120f, 440f)
                quadTo(120f, 423f, 131.5f, 411.5f)
                quadTo(143f, 400f, 160f, 400f)
                quadTo(177f, 400f, 188.5f, 411.5f)
                quadTo(200f, 423f, 200f, 440f)
                lineTo(200f, 520f)
                quadTo(200f, 537f, 188.5f, 548.5f)
                quadTo(177f, 560f, 160f, 560f)
                quadTo(143f, 560f, 131.5f, 548.5f)
                quadTo(120f, 537f, 120f, 520f)
                close()
                moveTo(600f, 680f)
                lineTo(600f, 280f)
                quadTo(600f, 263f, 611.5f, 251.5f)
                quadTo(623f, 240f, 640f, 240f)
                quadTo(657f, 240f, 668.5f, 251.5f)
                quadTo(680f, 263f, 680f, 280f)
                lineTo(680f, 680f)
                quadTo(680f, 697f, 668.5f, 708.5f)
                quadTo(657f, 720f, 640f, 720f)
                quadTo(623f, 720f, 611.5f, 708.5f)
                quadTo(600f, 697f, 600f, 680f)
                close()
                moveTo(760f, 520f)
                lineTo(760f, 440f)
                quadTo(760f, 423f, 771.5f, 411.5f)
                quadTo(783f, 400f, 800f, 400f)
                quadTo(817f, 400f, 828.5f, 411.5f)
                quadTo(840f, 423f, 840f, 440f)
                lineTo(840f, 520f)
                quadTo(840f, 537f, 828.5f, 548.5f)
                quadTo(817f, 560f, 800f, 560f)
                quadTo(783f, 560f, 771.5f, 548.5f)
                quadTo(760f, 537f, 760f, 520f)
                close()
            }
        }.build()

        return _GraphicEq!!
    }

@Suppress("ObjectPropertyName")
private var _GraphicEq: ImageVector? = null
