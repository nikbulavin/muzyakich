package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.PlayArrow: ImageVector
    get() {
        if (_PlayArrow != null) {
            return _PlayArrow!!
        }
        _PlayArrow = ImageVector.Builder(
            name = "Rounded.PlayArrow",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(320f, 687f)
                lineTo(320f, 273f)
                quadTo(320f, 256f, 332f, 244.5f)
                quadTo(344f, 233f, 360f, 233f)
                quadTo(365f, 233f, 370.5f, 234.5f)
                quadTo(376f, 236f, 381f, 239f)
                lineTo(707f, 446f)
                quadTo(716f, 452f, 720.5f, 461f)
                quadTo(725f, 470f, 725f, 480f)
                quadTo(725f, 490f, 720.5f, 499f)
                quadTo(716f, 508f, 707f, 514f)
                lineTo(381f, 721f)
                quadTo(376f, 724f, 370.5f, 725.5f)
                quadTo(365f, 727f, 360f, 727f)
                quadTo(344f, 727f, 332f, 715.5f)
                quadTo(320f, 704f, 320f, 687f)
                close()
            }
        }.build()

        return _PlayArrow!!
    }

@Suppress("ObjectPropertyName")
private var _PlayArrow: ImageVector? = null
