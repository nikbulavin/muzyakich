package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.Add: ImageVector
    get() {
        if (_Add != null) {
            return _Add!!
        }
        _Add = ImageVector.Builder(
            name = "Rounded.Add",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(451.5f, 828.5f)
                quadTo(440f, 817f, 440f, 800f)
                lineTo(440f, 520f)
                lineTo(160f, 520f)
                quadTo(143f, 520f, 131.5f, 508.5f)
                quadTo(120f, 497f, 120f, 480f)
                quadTo(120f, 463f, 131.5f, 451.5f)
                quadTo(143f, 440f, 160f, 440f)
                lineTo(440f, 440f)
                lineTo(440f, 160f)
                quadTo(440f, 143f, 451.5f, 131.5f)
                quadTo(463f, 120f, 480f, 120f)
                quadTo(497f, 120f, 508.5f, 131.5f)
                quadTo(520f, 143f, 520f, 160f)
                lineTo(520f, 440f)
                lineTo(800f, 440f)
                quadTo(817f, 440f, 828.5f, 451.5f)
                quadTo(840f, 463f, 840f, 480f)
                quadTo(840f, 497f, 828.5f, 508.5f)
                quadTo(817f, 520f, 800f, 520f)
                lineTo(520f, 520f)
                lineTo(520f, 800f)
                quadTo(520f, 817f, 508.5f, 828.5f)
                quadTo(497f, 840f, 480f, 840f)
                quadTo(463f, 840f, 451.5f, 828.5f)
                close()
            }
        }.build()

        return _Add!!
    }

@Suppress("ObjectPropertyName")
private var _Add: ImageVector? = null
