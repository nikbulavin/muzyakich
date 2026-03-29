package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.Flag: ImageVector
    get() {
        if (_Flag != null) {
            return _Flag!!
        }
        _Flag = ImageVector.Builder(
            name = "Filled.Flag",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(280f, 520f)
                lineTo(280f, 840f)
                quadTo(280f, 857f, 268.5f, 868.5f)
                quadTo(257f, 880f, 240f, 880f)
                quadTo(223f, 880f, 211.5f, 868.5f)
                quadTo(200f, 857f, 200f, 840f)
                lineTo(200f, 160f)
                quadTo(200f, 143f, 211.5f, 131.5f)
                quadTo(223f, 120f, 240f, 120f)
                lineTo(781f, 120f)
                quadTo(792f, 120f, 800.5f, 125f)
                quadTo(809f, 130f, 814f, 138f)
                quadTo(819f, 146f, 820.5f, 155.5f)
                quadTo(822f, 165f, 818f, 175f)
                lineTo(760f, 320f)
                lineTo(818f, 465f)
                quadTo(822f, 475f, 820.5f, 484.5f)
                quadTo(819f, 494f, 814f, 502f)
                quadTo(809f, 510f, 800.5f, 515f)
                quadTo(792f, 520f, 781f, 520f)
                lineTo(280f, 520f)
                close()
            }
        }.build()

        return _Flag!!
    }

@Suppress("ObjectPropertyName")
private var _Flag: ImageVector? = null
