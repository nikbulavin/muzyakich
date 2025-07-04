package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.DragHandle: ImageVector
    get() {
        if (_DragHandle != null) {
            return _DragHandle!!
        }
        _DragHandle = ImageVector.Builder(
            name = "Rounded.DragHandle",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(200f, 600f)
                quadTo(183f, 600f, 171.5f, 588.5f)
                quadTo(160f, 577f, 160f, 560f)
                quadTo(160f, 543f, 171.5f, 531.5f)
                quadTo(183f, 520f, 200f, 520f)
                lineTo(760f, 520f)
                quadTo(777f, 520f, 788.5f, 531.5f)
                quadTo(800f, 543f, 800f, 560f)
                quadTo(800f, 577f, 788.5f, 588.5f)
                quadTo(777f, 600f, 760f, 600f)
                lineTo(200f, 600f)
                close()
                moveTo(200f, 440f)
                quadTo(183f, 440f, 171.5f, 428.5f)
                quadTo(160f, 417f, 160f, 400f)
                quadTo(160f, 383f, 171.5f, 371.5f)
                quadTo(183f, 360f, 200f, 360f)
                lineTo(760f, 360f)
                quadTo(777f, 360f, 788.5f, 371.5f)
                quadTo(800f, 383f, 800f, 400f)
                quadTo(800f, 417f, 788.5f, 428.5f)
                quadTo(777f, 440f, 760f, 440f)
                lineTo(200f, 440f)
                close()
            }
        }.build()

        return _DragHandle!!
    }

@Suppress("ObjectPropertyName")
private var _DragHandle: ImageVector? = null
