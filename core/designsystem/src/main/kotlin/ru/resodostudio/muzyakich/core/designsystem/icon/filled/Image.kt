package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.Image: ImageVector
    get() {
        if (_Image != null) {
            return _Image!!
        }
        _Image = ImageVector.Builder(
            name = "Filled.Image",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(200f, 840f)
                quadTo(167f, 840f, 143.5f, 816.5f)
                quadTo(120f, 793f, 120f, 760f)
                lineTo(120f, 200f)
                quadTo(120f, 167f, 143.5f, 143.5f)
                quadTo(167f, 120f, 200f, 120f)
                lineTo(760f, 120f)
                quadTo(793f, 120f, 816.5f, 143.5f)
                quadTo(840f, 167f, 840f, 200f)
                lineTo(840f, 760f)
                quadTo(840f, 793f, 816.5f, 816.5f)
                quadTo(793f, 840f, 760f, 840f)
                lineTo(200f, 840f)
                close()
                moveTo(280f, 680f)
                lineTo(680f, 680f)
                quadTo(692f, 680f, 698f, 669f)
                quadTo(704f, 658f, 696f, 648f)
                lineTo(586f, 501f)
                quadTo(580f, 493f, 570f, 493f)
                quadTo(560f, 493f, 554f, 501f)
                lineTo(450f, 640f)
                lineTo(376f, 541f)
                quadTo(370f, 533f, 360f, 533f)
                quadTo(350f, 533f, 344f, 541f)
                lineTo(264f, 648f)
                quadTo(256f, 658f, 262f, 669f)
                quadTo(268f, 680f, 280f, 680f)
                close()
            }
        }.build()

        return _Image!!
    }

@Suppress("ObjectPropertyName")
private var _Image: ImageVector? = null
