package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.Delete: ImageVector
    get() {
        if (_Delete != null) {
            return _Delete!!
        }
        _Delete = ImageVector.Builder(
            name = "Filled.Delete",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(280f, 840f)
                quadTo(247f, 840f, 223.5f, 816.5f)
                quadTo(200f, 793f, 200f, 760f)
                lineTo(200f, 240f)
                lineTo(200f, 240f)
                quadTo(183f, 240f, 171.5f, 228.5f)
                quadTo(160f, 217f, 160f, 200f)
                quadTo(160f, 183f, 171.5f, 171.5f)
                quadTo(183f, 160f, 200f, 160f)
                lineTo(360f, 160f)
                lineTo(360f, 160f)
                quadTo(360f, 143f, 371.5f, 131.5f)
                quadTo(383f, 120f, 400f, 120f)
                lineTo(560f, 120f)
                quadTo(577f, 120f, 588.5f, 131.5f)
                quadTo(600f, 143f, 600f, 160f)
                lineTo(600f, 160f)
                lineTo(760f, 160f)
                quadTo(777f, 160f, 788.5f, 171.5f)
                quadTo(800f, 183f, 800f, 200f)
                quadTo(800f, 217f, 788.5f, 228.5f)
                quadTo(777f, 240f, 760f, 240f)
                lineTo(760f, 240f)
                lineTo(760f, 760f)
                quadTo(760f, 793f, 736.5f, 816.5f)
                quadTo(713f, 840f, 680f, 840f)
                lineTo(280f, 840f)
                close()
                moveTo(400f, 680f)
                quadTo(417f, 680f, 428.5f, 668.5f)
                quadTo(440f, 657f, 440f, 640f)
                lineTo(440f, 360f)
                quadTo(440f, 343f, 428.5f, 331.5f)
                quadTo(417f, 320f, 400f, 320f)
                quadTo(383f, 320f, 371.5f, 331.5f)
                quadTo(360f, 343f, 360f, 360f)
                lineTo(360f, 640f)
                quadTo(360f, 657f, 371.5f, 668.5f)
                quadTo(383f, 680f, 400f, 680f)
                close()
                moveTo(560f, 680f)
                quadTo(577f, 680f, 588.5f, 668.5f)
                quadTo(600f, 657f, 600f, 640f)
                lineTo(600f, 360f)
                quadTo(600f, 343f, 588.5f, 331.5f)
                quadTo(577f, 320f, 560f, 320f)
                quadTo(543f, 320f, 531.5f, 331.5f)
                quadTo(520f, 343f, 520f, 360f)
                lineTo(520f, 640f)
                quadTo(520f, 657f, 531.5f, 668.5f)
                quadTo(543f, 680f, 560f, 680f)
                close()
            }
        }.build()

        return _Delete!!
    }

@Suppress("ObjectPropertyName")
private var _Delete: ImageVector? = null
