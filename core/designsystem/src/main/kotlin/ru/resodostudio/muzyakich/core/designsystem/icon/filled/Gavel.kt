package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.Gavel: ImageVector
    get() {
        if (_Gavel != null) {
            return _Gavel!!
        }
        _Gavel = ImageVector.Builder(
            name = "Filled.Gavel",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(200f, 760f)
                lineTo(600f, 760f)
                quadTo(617f, 760f, 628.5f, 771.5f)
                quadTo(640f, 783f, 640f, 800f)
                quadTo(640f, 817f, 628.5f, 828.5f)
                quadTo(617f, 840f, 600f, 840f)
                lineTo(200f, 840f)
                quadTo(183f, 840f, 171.5f, 828.5f)
                quadTo(160f, 817f, 160f, 800f)
                quadTo(160f, 783f, 171.5f, 771.5f)
                quadTo(183f, 760f, 200f, 760f)
                close()
                moveTo(329f, 589f)
                lineTo(216f, 476f)
                quadTo(193f, 453f, 192.5f, 419.5f)
                quadTo(192f, 386f, 215f, 363f)
                lineTo(244f, 334f)
                lineTo(472f, 560f)
                lineTo(443f, 589f)
                quadTo(420f, 612f, 386f, 612f)
                quadTo(352f, 612f, 329f, 589f)
                close()
                moveTo(640f, 392f)
                lineTo(414f, 164f)
                lineTo(443f, 135f)
                quadTo(466f, 112f, 499.5f, 112.5f)
                quadTo(533f, 113f, 556f, 136f)
                lineTo(669f, 249f)
                quadTo(692f, 272f, 692f, 306f)
                quadTo(692f, 340f, 669f, 363f)
                lineTo(640f, 392f)
                close()
                moveTo(796f, 772f)
                lineTo(302f, 278f)
                lineTo(358f, 222f)
                lineTo(852f, 716f)
                quadTo(863f, 727f, 863f, 744f)
                quadTo(863f, 761f, 852f, 772f)
                quadTo(841f, 783f, 824f, 783f)
                quadTo(807f, 783f, 796f, 772f)
                close()
            }
        }.build()

        return _Gavel!!
    }

@Suppress("ObjectPropertyName")
private var _Gavel: ImageVector? = null
