package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.SortByAlpha: ImageVector
    get() {
        if (_SortByAlpha != null) {
            return _SortByAlpha!!
        }
        _SortByAlpha = ImageVector.Builder(
            name = "Rounded.SortByAlpha",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(196f, 584f)
                lineTo(173f, 654f)
                quadTo(169f, 665f, 159f, 672.5f)
                quadTo(149f, 680f, 137f, 680f)
                quadTo(117f, 680f, 104.5f, 663.5f)
                quadTo(92f, 647f, 100f, 627f)
                lineTo(220f, 306f)
                quadTo(225f, 294f, 235f, 287f)
                quadTo(245f, 280f, 258f, 280f)
                lineTo(288f, 280f)
                quadTo(301f, 280f, 311f, 287f)
                quadTo(321f, 294f, 326f, 306f)
                lineTo(447f, 629f)
                quadTo(454f, 648f, 442.5f, 664f)
                quadTo(431f, 680f, 411f, 680f)
                quadTo(399f, 680f, 389f, 672.5f)
                quadTo(379f, 665f, 375f, 654f)
                lineTo(350f, 584f)
                lineTo(196f, 584f)
                close()
                moveTo(220f, 516f)
                lineTo(324f, 516f)
                lineTo(276f, 366f)
                lineTo(270f, 366f)
                lineTo(220f, 516f)
                close()
                moveTo(638f, 608f)
                lineTo(804f, 608f)
                quadTo(819f, 608f, 829.5f, 618.5f)
                quadTo(840f, 629f, 840f, 644f)
                quadTo(840f, 659f, 829.5f, 669.5f)
                quadTo(819f, 680f, 804f, 680f)
                lineTo(572f, 680f)
                quadTo(562f, 680f, 555f, 673f)
                quadTo(548f, 666f, 548f, 656f)
                lineTo(548f, 618f)
                quadTo(548f, 611f, 550f, 604.5f)
                quadTo(552f, 598f, 557f, 593f)
                lineTo(750f, 352f)
                lineTo(592f, 352f)
                quadTo(577f, 352f, 566.5f, 341.5f)
                quadTo(556f, 331f, 556f, 316f)
                quadTo(556f, 301f, 566.5f, 290.5f)
                quadTo(577f, 280f, 592f, 280f)
                lineTo(814f, 280f)
                quadTo(824f, 280f, 831f, 287f)
                quadTo(838f, 294f, 838f, 304f)
                lineTo(838f, 342f)
                quadTo(838f, 349f, 836f, 355.5f)
                quadTo(834f, 362f, 829f, 367f)
                lineTo(638f, 608f)
                close()
                moveTo(384f, 200f)
                quadTo(377f, 200f, 374.5f, 194f)
                quadTo(372f, 188f, 377f, 183f)
                lineTo(466f, 94f)
                quadTo(472f, 88f, 480f, 88f)
                quadTo(488f, 88f, 494f, 94f)
                lineTo(583f, 183f)
                quadTo(588f, 188f, 585.5f, 194f)
                quadTo(583f, 200f, 576f, 200f)
                lineTo(384f, 200f)
                close()
                moveTo(466f, 866f)
                lineTo(377f, 777f)
                quadTo(372f, 772f, 374.5f, 766f)
                quadTo(377f, 760f, 384f, 760f)
                lineTo(576f, 760f)
                quadTo(583f, 760f, 585.5f, 766f)
                quadTo(588f, 772f, 583f, 777f)
                lineTo(494f, 866f)
                quadTo(488f, 872f, 480f, 872f)
                quadTo(472f, 872f, 466f, 866f)
                close()
            }
        }.build()

        return _SortByAlpha!!
    }

@Suppress("ObjectPropertyName")
private var _SortByAlpha: ImageVector? = null
