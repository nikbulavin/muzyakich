package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.SkipPrevious: ImageVector
    get() {
        if (_SkipPrevious != null) {
            return _SkipPrevious!!
        }
        _SkipPrevious = ImageVector.Builder(
            name = "Rounded.SkipPrevious",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(220f, 680f)
                lineTo(220f, 280f)
                quadTo(220f, 263f, 231.5f, 251.5f)
                quadTo(243f, 240f, 260f, 240f)
                quadTo(277f, 240f, 288.5f, 251.5f)
                quadTo(300f, 263f, 300f, 280f)
                lineTo(300f, 680f)
                quadTo(300f, 697f, 288.5f, 708.5f)
                quadTo(277f, 720f, 260f, 720f)
                quadTo(243f, 720f, 231.5f, 708.5f)
                quadTo(220f, 697f, 220f, 680f)
                close()
                moveTo(678f, 679f)
                lineTo(430f, 513f)
                quadTo(421f, 507f, 416.5f, 498.5f)
                quadTo(412f, 490f, 412f, 480f)
                quadTo(412f, 470f, 416.5f, 461.5f)
                quadTo(421f, 453f, 430f, 447f)
                lineTo(678f, 281f)
                quadTo(683f, 277f, 689f, 276f)
                quadTo(695f, 275f, 700f, 275f)
                quadTo(716f, 275f, 728f, 286f)
                quadTo(740f, 297f, 740f, 315f)
                lineTo(740f, 645f)
                quadTo(740f, 663f, 728f, 674f)
                quadTo(716f, 685f, 700f, 685f)
                quadTo(695f, 685f, 689f, 684f)
                quadTo(683f, 683f, 678f, 679f)
                close()
            }
        }.build()

        return _SkipPrevious!!
    }

@Suppress("ObjectPropertyName")
private var _SkipPrevious: ImageVector? = null
