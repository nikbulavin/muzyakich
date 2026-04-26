package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.ArrowRight: ImageVector
    get() {
        if (_ArrowRight != null) {
            return _ArrowRight!!
        }
        _ArrowRight = ImageVector.Builder(
            name = "Rounded.ArrowRight",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
            autoMirror = true,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(420f, 652f)
                quadTo(412f, 652f, 406f, 646.5f)
                quadTo(400f, 641f, 400f, 632f)
                lineTo(400f, 328f)
                quadTo(400f, 319f, 406f, 313.5f)
                quadTo(412f, 308f, 420f, 308f)
                quadTo(422f, 308f, 434f, 314f)
                lineTo(579f, 459f)
                quadTo(584f, 464f, 586f, 469f)
                quadTo(588f, 474f, 588f, 480f)
                quadTo(588f, 486f, 586f, 491f)
                quadTo(584f, 496f, 579f, 501f)
                lineTo(434f, 646f)
                quadTo(431f, 649f, 427.5f, 650.5f)
                quadTo(424f, 652f, 420f, 652f)
                close()
            }
        }.build()

        return _ArrowRight!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowRight: ImageVector? = null
