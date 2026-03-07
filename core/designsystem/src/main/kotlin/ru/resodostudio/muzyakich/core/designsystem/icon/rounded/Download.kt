package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.Download: ImageVector
    get() {
        if (_Download != null) {
            return _Download!!
        }
        _Download = ImageVector.Builder(
            name = "Rounded.Download",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(465f, 620.5f)
                quadTo(458f, 618f, 452f, 612f)
                lineTo(308f, 468f)
                quadTo(296f, 456f, 296.5f, 440f)
                quadTo(297f, 424f, 308f, 412f)
                quadTo(320f, 400f, 336.5f, 399.5f)
                quadTo(353f, 399f, 365f, 411f)
                lineTo(440f, 486f)
                lineTo(440f, 200f)
                quadTo(440f, 183f, 451.5f, 171.5f)
                quadTo(463f, 160f, 480f, 160f)
                quadTo(497f, 160f, 508.5f, 171.5f)
                quadTo(520f, 183f, 520f, 200f)
                lineTo(520f, 486f)
                lineTo(595f, 411f)
                quadTo(607f, 399f, 623.5f, 399.5f)
                quadTo(640f, 400f, 652f, 412f)
                quadTo(663f, 424f, 663.5f, 440f)
                quadTo(664f, 456f, 652f, 468f)
                lineTo(508f, 612f)
                quadTo(502f, 618f, 495f, 620.5f)
                quadTo(488f, 623f, 480f, 623f)
                quadTo(472f, 623f, 465f, 620.5f)
                close()
                moveTo(240f, 800f)
                quadTo(207f, 800f, 183.5f, 776.5f)
                quadTo(160f, 753f, 160f, 720f)
                lineTo(160f, 640f)
                quadTo(160f, 623f, 171.5f, 611.5f)
                quadTo(183f, 600f, 200f, 600f)
                quadTo(217f, 600f, 228.5f, 611.5f)
                quadTo(240f, 623f, 240f, 640f)
                lineTo(240f, 720f)
                quadTo(240f, 720f, 240f, 720f)
                quadTo(240f, 720f, 240f, 720f)
                lineTo(720f, 720f)
                quadTo(720f, 720f, 720f, 720f)
                quadTo(720f, 720f, 720f, 720f)
                lineTo(720f, 640f)
                quadTo(720f, 623f, 731.5f, 611.5f)
                quadTo(743f, 600f, 760f, 600f)
                quadTo(777f, 600f, 788.5f, 611.5f)
                quadTo(800f, 623f, 800f, 640f)
                lineTo(800f, 720f)
                quadTo(800f, 753f, 776.5f, 776.5f)
                quadTo(753f, 800f, 720f, 800f)
                lineTo(240f, 800f)
                close()
            }
        }.build()

        return _Download!!
    }

@Suppress("ObjectPropertyName")
private var _Download: ImageVector? = null
