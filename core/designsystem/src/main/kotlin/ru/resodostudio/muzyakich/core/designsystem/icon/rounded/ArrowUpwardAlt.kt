package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.ArrowUpwardAlt: ImageVector
    get() {
        if (_ArrowUpwardAlt != null) {
            return _ArrowUpwardAlt!!
        }
        _ArrowUpwardAlt = ImageVector.Builder(
            name = "Rounded.ArrowUpwardAlt",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(440f, 352f)
                lineTo(324f, 468f)
                quadTo(313f, 479f, 296f, 479f)
                quadTo(279f, 479f, 268f, 468f)
                quadTo(257f, 457f, 257f, 440f)
                quadTo(257f, 423f, 268f, 412f)
                lineTo(452f, 228f)
                quadTo(464f, 216f, 480f, 216f)
                quadTo(496f, 216f, 508f, 228f)
                lineTo(692f, 412f)
                quadTo(703f, 423f, 703f, 440f)
                quadTo(703f, 457f, 692f, 468f)
                quadTo(681f, 479f, 664f, 479f)
                quadTo(647f, 479f, 636f, 468f)
                lineTo(520f, 352f)
                lineTo(520f, 680f)
                quadTo(520f, 697f, 508.5f, 708.5f)
                quadTo(497f, 720f, 480f, 720f)
                quadTo(463f, 720f, 451.5f, 708.5f)
                quadTo(440f, 697f, 440f, 680f)
                lineTo(440f, 352f)
                close()
            }
        }.build()

        return _ArrowUpwardAlt!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowUpwardAlt: ImageVector? = null
