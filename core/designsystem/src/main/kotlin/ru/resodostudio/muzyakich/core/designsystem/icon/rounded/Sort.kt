package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.Sort: ImageVector
    get() {
        if (_Sort != null) {
            return _Sort!!
        }
        _Sort = ImageVector.Builder(
            name = "Rounded.Sort",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
            autoMirror = true,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(160f, 720f)
                quadTo(143f, 720f, 131.5f, 708.5f)
                quadTo(120f, 697f, 120f, 680f)
                quadTo(120f, 663f, 131.5f, 651.5f)
                quadTo(143f, 640f, 160f, 640f)
                lineTo(320f, 640f)
                quadTo(337f, 640f, 348.5f, 651.5f)
                quadTo(360f, 663f, 360f, 680f)
                quadTo(360f, 697f, 348.5f, 708.5f)
                quadTo(337f, 720f, 320f, 720f)
                lineTo(160f, 720f)
                close()
                moveTo(160f, 520f)
                quadTo(143f, 520f, 131.5f, 508.5f)
                quadTo(120f, 497f, 120f, 480f)
                quadTo(120f, 463f, 131.5f, 451.5f)
                quadTo(143f, 440f, 160f, 440f)
                lineTo(560f, 440f)
                quadTo(577f, 440f, 588.5f, 451.5f)
                quadTo(600f, 463f, 600f, 480f)
                quadTo(600f, 497f, 588.5f, 508.5f)
                quadTo(577f, 520f, 560f, 520f)
                lineTo(160f, 520f)
                close()
                moveTo(160f, 320f)
                quadTo(143f, 320f, 131.5f, 308.5f)
                quadTo(120f, 297f, 120f, 280f)
                quadTo(120f, 263f, 131.5f, 251.5f)
                quadTo(143f, 240f, 160f, 240f)
                lineTo(800f, 240f)
                quadTo(817f, 240f, 828.5f, 251.5f)
                quadTo(840f, 263f, 840f, 280f)
                quadTo(840f, 297f, 828.5f, 308.5f)
                quadTo(817f, 320f, 800f, 320f)
                lineTo(160f, 320f)
                close()
            }
        }.build()

        return _Sort!!
    }

@Suppress("ObjectPropertyName")
private var _Sort: ImageVector? = null
