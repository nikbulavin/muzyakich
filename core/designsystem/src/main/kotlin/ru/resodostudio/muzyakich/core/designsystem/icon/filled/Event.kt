package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.Event: ImageVector
    get() {
        if (_Event != null) {
            return _Event!!
        }
        _Event = ImageVector.Builder(
            name = "Filled.Event",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(509f, 691f)
                quadTo(480f, 662f, 480f, 620f)
                quadTo(480f, 578f, 509f, 549f)
                quadTo(538f, 520f, 580f, 520f)
                quadTo(622f, 520f, 651f, 549f)
                quadTo(680f, 578f, 680f, 620f)
                quadTo(680f, 662f, 651f, 691f)
                quadTo(622f, 720f, 580f, 720f)
                quadTo(538f, 720f, 509f, 691f)
                close()
                moveTo(200f, 880f)
                quadTo(167f, 880f, 143.5f, 856.5f)
                quadTo(120f, 833f, 120f, 800f)
                lineTo(120f, 240f)
                quadTo(120f, 207f, 143.5f, 183.5f)
                quadTo(167f, 160f, 200f, 160f)
                lineTo(240f, 160f)
                lineTo(240f, 120f)
                quadTo(240f, 103f, 251.5f, 91.5f)
                quadTo(263f, 80f, 280f, 80f)
                quadTo(297f, 80f, 308.5f, 91.5f)
                quadTo(320f, 103f, 320f, 120f)
                lineTo(320f, 160f)
                lineTo(640f, 160f)
                lineTo(640f, 120f)
                quadTo(640f, 103f, 651.5f, 91.5f)
                quadTo(663f, 80f, 680f, 80f)
                quadTo(697f, 80f, 708.5f, 91.5f)
                quadTo(720f, 103f, 720f, 120f)
                lineTo(720f, 160f)
                lineTo(760f, 160f)
                quadTo(793f, 160f, 816.5f, 183.5f)
                quadTo(840f, 207f, 840f, 240f)
                lineTo(840f, 800f)
                quadTo(840f, 833f, 816.5f, 856.5f)
                quadTo(793f, 880f, 760f, 880f)
                lineTo(200f, 880f)
                close()
                moveTo(200f, 800f)
                lineTo(760f, 800f)
                quadTo(760f, 800f, 760f, 800f)
                quadTo(760f, 800f, 760f, 800f)
                lineTo(760f, 400f)
                lineTo(200f, 400f)
                lineTo(200f, 800f)
                quadTo(200f, 800f, 200f, 800f)
                quadTo(200f, 800f, 200f, 800f)
                close()
            }
        }.build()

        return _Event!!
    }

@Suppress("ObjectPropertyName")
private var _Event: ImageVector? = null
