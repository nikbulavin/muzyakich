package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.Pause: ImageVector
    get() {
        if (_Pause != null) {
            return _Pause!!
        }
        _Pause = ImageVector.Builder(
            name = "Rounded.Pause",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(640f, 760f)
                quadTo(607f, 760f, 583.5f, 736.5f)
                quadTo(560f, 713f, 560f, 680f)
                lineTo(560f, 280f)
                quadTo(560f, 247f, 583.5f, 223.5f)
                quadTo(607f, 200f, 640f, 200f)
                lineTo(640f, 200f)
                quadTo(673f, 200f, 696.5f, 223.5f)
                quadTo(720f, 247f, 720f, 280f)
                lineTo(720f, 680f)
                quadTo(720f, 713f, 696.5f, 736.5f)
                quadTo(673f, 760f, 640f, 760f)
                close()
                moveTo(320f, 760f)
                quadTo(287f, 760f, 263.5f, 736.5f)
                quadTo(240f, 713f, 240f, 680f)
                lineTo(240f, 280f)
                quadTo(240f, 247f, 263.5f, 223.5f)
                quadTo(287f, 200f, 320f, 200f)
                lineTo(320f, 200f)
                quadTo(353f, 200f, 376.5f, 223.5f)
                quadTo(400f, 247f, 400f, 280f)
                lineTo(400f, 680f)
                quadTo(400f, 713f, 376.5f, 736.5f)
                quadTo(353f, 760f, 320f, 760f)
                close()
            }
        }.build()

        return _Pause!!
    }

@Suppress("ObjectPropertyName")
private var _Pause: ImageVector? = null
