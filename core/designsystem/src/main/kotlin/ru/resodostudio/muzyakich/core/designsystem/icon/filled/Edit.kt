package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.Edit: ImageVector
    get() {
        if (_Edit != null) {
            return _Edit!!
        }
        _Edit = ImageVector.Builder(
            name = "Filled.Edit",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(160f, 840f)
                quadTo(143f, 840f, 131.5f, 828.5f)
                quadTo(120f, 817f, 120f, 800f)
                lineTo(120f, 703f)
                quadTo(120f, 687f, 126f, 672.5f)
                quadTo(132f, 658f, 143f, 647f)
                lineTo(648f, 143f)
                quadTo(660f, 132f, 674.5f, 126f)
                quadTo(689f, 120f, 705f, 120f)
                quadTo(721f, 120f, 736f, 126f)
                quadTo(751f, 132f, 762f, 144f)
                lineTo(817f, 200f)
                quadTo(829f, 211f, 834.5f, 226f)
                quadTo(840f, 241f, 840f, 256f)
                quadTo(840f, 272f, 834.5f, 286.5f)
                quadTo(829f, 301f, 817f, 313f)
                lineTo(313f, 817f)
                quadTo(302f, 828f, 287.5f, 834f)
                quadTo(273f, 840f, 257f, 840f)
                lineTo(160f, 840f)
                close()
                moveTo(704f, 312f)
                lineTo(760f, 256f)
                lineTo(704f, 200f)
                lineTo(648f, 256f)
                lineTo(704f, 312f)
                close()
            }
        }.build()

        return _Edit!!
    }

@Suppress("ObjectPropertyName")
private var _Edit: ImageVector? = null
