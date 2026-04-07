package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.Remove: ImageVector
    get() {
        if (_Remove != null) {
            return _Remove!!
        }
        _Remove = ImageVector.Builder(
            name = "Filled.Remove",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(240f, 520f)
                quadTo(223f, 520f, 211.5f, 508.5f)
                quadTo(200f, 497f, 200f, 480f)
                quadTo(200f, 463f, 211.5f, 451.5f)
                quadTo(223f, 440f, 240f, 440f)
                lineTo(720f, 440f)
                quadTo(737f, 440f, 748.5f, 451.5f)
                quadTo(760f, 463f, 760f, 480f)
                quadTo(760f, 497f, 748.5f, 508.5f)
                quadTo(737f, 520f, 720f, 520f)
                lineTo(240f, 520f)
                close()
            }
        }.build()

        return _Remove!!
    }

@Suppress("ObjectPropertyName")
private var _Remove: ImageVector? = null
