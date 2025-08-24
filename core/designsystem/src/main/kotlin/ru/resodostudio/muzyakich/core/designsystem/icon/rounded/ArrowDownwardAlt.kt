package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.ArrowDownwardAlt: ImageVector
    get() {
        if (_ArrowDownwardAlt != null) {
            return _ArrowDownwardAlt!!
        }
        _ArrowDownwardAlt = ImageVector.Builder(
            name = "Rounded.ArrowDownwardAlt",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(440f, 568f)
                lineTo(440f, 240f)
                quadTo(440f, 223f, 451.5f, 211.5f)
                quadTo(463f, 200f, 480f, 200f)
                quadTo(497f, 200f, 508.5f, 211.5f)
                quadTo(520f, 223f, 520f, 240f)
                lineTo(520f, 568f)
                lineTo(636f, 452f)
                quadTo(647f, 441f, 664f, 441f)
                quadTo(681f, 441f, 692f, 452f)
                quadTo(703f, 463f, 703f, 480f)
                quadTo(703f, 497f, 692f, 508f)
                lineTo(508f, 692f)
                quadTo(496f, 704f, 480f, 704f)
                quadTo(464f, 704f, 452f, 692f)
                lineTo(268f, 508f)
                quadTo(257f, 497f, 257f, 480f)
                quadTo(257f, 463f, 268f, 452f)
                quadTo(279f, 441f, 296f, 441f)
                quadTo(313f, 441f, 324f, 452f)
                lineTo(440f, 568f)
                close()
            }
        }.build()

        return _ArrowDownwardAlt!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowDownwardAlt: ImageVector? = null
