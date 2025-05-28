package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.SkipNext: ImageVector
    get() {
        if (_SkipNext != null) {
            return _SkipNext!!
        }
        _SkipNext = ImageVector.Builder(
            name = "Rounded.SkipNext",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(660f, 680f)
                lineTo(660f, 280f)
                quadTo(660f, 263f, 671.5f, 251.5f)
                quadTo(683f, 240f, 700f, 240f)
                quadTo(717f, 240f, 728.5f, 251.5f)
                quadTo(740f, 263f, 740f, 280f)
                lineTo(740f, 680f)
                quadTo(740f, 697f, 728.5f, 708.5f)
                quadTo(717f, 720f, 700f, 720f)
                quadTo(683f, 720f, 671.5f, 708.5f)
                quadTo(660f, 697f, 660f, 680f)
                close()
                moveTo(220f, 645f)
                lineTo(220f, 315f)
                quadTo(220f, 297f, 232f, 286f)
                quadTo(244f, 275f, 260f, 275f)
                quadTo(265f, 275f, 271f, 276f)
                quadTo(277f, 277f, 282f, 281f)
                lineTo(530f, 447f)
                quadTo(539f, 453f, 543.5f, 461.5f)
                quadTo(548f, 470f, 548f, 480f)
                quadTo(548f, 490f, 543.5f, 498.5f)
                quadTo(539f, 507f, 530f, 513f)
                lineTo(282f, 679f)
                quadTo(277f, 683f, 271f, 684f)
                quadTo(265f, 685f, 260f, 685f)
                quadTo(244f, 685f, 232f, 674f)
                quadTo(220f, 663f, 220f, 645f)
                close()
            }
        }.build()

        return _SkipNext!!
    }

@Suppress("ObjectPropertyName")
private var _SkipNext: ImageVector? = null
