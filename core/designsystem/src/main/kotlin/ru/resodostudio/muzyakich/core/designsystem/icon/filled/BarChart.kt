package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.BarChart: ImageVector
    get() {
        if (_BarChart != null) {
            return _BarChart!!
        }
        _BarChart = ImageVector.Builder(
            name = "Filled.BarChart",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(680f, 800f)
                quadTo(663f, 800f, 651.5f, 788.5f)
                quadTo(640f, 777f, 640f, 760f)
                lineTo(640f, 560f)
                quadTo(640f, 543f, 651.5f, 531.5f)
                quadTo(663f, 520f, 680f, 520f)
                lineTo(760f, 520f)
                quadTo(777f, 520f, 788.5f, 531.5f)
                quadTo(800f, 543f, 800f, 560f)
                lineTo(800f, 760f)
                quadTo(800f, 777f, 788.5f, 788.5f)
                quadTo(777f, 800f, 760f, 800f)
                lineTo(680f, 800f)
                close()
                moveTo(440f, 800f)
                quadTo(423f, 800f, 411.5f, 788.5f)
                quadTo(400f, 777f, 400f, 760f)
                lineTo(400f, 200f)
                quadTo(400f, 183f, 411.5f, 171.5f)
                quadTo(423f, 160f, 440f, 160f)
                lineTo(520f, 160f)
                quadTo(537f, 160f, 548.5f, 171.5f)
                quadTo(560f, 183f, 560f, 200f)
                lineTo(560f, 760f)
                quadTo(560f, 777f, 548.5f, 788.5f)
                quadTo(537f, 800f, 520f, 800f)
                lineTo(440f, 800f)
                close()
                moveTo(200f, 800f)
                quadTo(183f, 800f, 171.5f, 788.5f)
                quadTo(160f, 777f, 160f, 760f)
                lineTo(160f, 400f)
                quadTo(160f, 383f, 171.5f, 371.5f)
                quadTo(183f, 360f, 200f, 360f)
                lineTo(280f, 360f)
                quadTo(297f, 360f, 308.5f, 371.5f)
                quadTo(320f, 383f, 320f, 400f)
                lineTo(320f, 760f)
                quadTo(320f, 777f, 308.5f, 788.5f)
                quadTo(297f, 800f, 280f, 800f)
                lineTo(200f, 800f)
                close()
            }
        }.build()

        return _BarChart!!
    }

@Suppress("ObjectPropertyName")
private var _BarChart: ImageVector? = null
