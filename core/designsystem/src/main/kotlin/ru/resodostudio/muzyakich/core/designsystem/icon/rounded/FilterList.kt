package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.FilterList: ImageVector
    get() {
        if (_FilterList != null) {
            return _FilterList!!
        }
        _FilterList = ImageVector.Builder(
            name = "Rounded.FilterList",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(440f, 720f)
                quadTo(423f, 720f, 411.5f, 708.5f)
                quadTo(400f, 697f, 400f, 680f)
                quadTo(400f, 663f, 411.5f, 651.5f)
                quadTo(423f, 640f, 440f, 640f)
                lineTo(520f, 640f)
                quadTo(537f, 640f, 548.5f, 651.5f)
                quadTo(560f, 663f, 560f, 680f)
                quadTo(560f, 697f, 548.5f, 708.5f)
                quadTo(537f, 720f, 520f, 720f)
                lineTo(440f, 720f)
                close()
                moveTo(280f, 520f)
                quadTo(263f, 520f, 251.5f, 508.5f)
                quadTo(240f, 497f, 240f, 480f)
                quadTo(240f, 463f, 251.5f, 451.5f)
                quadTo(263f, 440f, 280f, 440f)
                lineTo(680f, 440f)
                quadTo(697f, 440f, 708.5f, 451.5f)
                quadTo(720f, 463f, 720f, 480f)
                quadTo(720f, 497f, 708.5f, 508.5f)
                quadTo(697f, 520f, 680f, 520f)
                lineTo(280f, 520f)
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

        return _FilterList!!
    }

@Suppress("ObjectPropertyName")
private var _FilterList: ImageVector? = null
