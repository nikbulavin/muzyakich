package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.Title: ImageVector
    get() {
        if (_Title != null) {
            return _Title!!
        }
        _Title = ImageVector.Builder(
            name = "Rounded.Title",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(420f, 280f)
                lineTo(260f, 280f)
                quadTo(235f, 280f, 217.5f, 262.5f)
                quadTo(200f, 245f, 200f, 220f)
                quadTo(200f, 195f, 217.5f, 177.5f)
                quadTo(235f, 160f, 260f, 160f)
                lineTo(700f, 160f)
                quadTo(725f, 160f, 742.5f, 177.5f)
                quadTo(760f, 195f, 760f, 220f)
                quadTo(760f, 245f, 742.5f, 262.5f)
                quadTo(725f, 280f, 700f, 280f)
                lineTo(540f, 280f)
                lineTo(540f, 740f)
                quadTo(540f, 765f, 522.5f, 782.5f)
                quadTo(505f, 800f, 480f, 800f)
                quadTo(455f, 800f, 437.5f, 782.5f)
                quadTo(420f, 765f, 420f, 740f)
                lineTo(420f, 280f)
                close()
            }
        }.build()

        return _Title!!
    }

@Suppress("ObjectPropertyName")
private var _Title: ImageVector? = null
