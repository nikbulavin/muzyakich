package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.PermMedia: ImageVector
    get() {
        if (_PermMedia != null) {
            return _PermMedia!!
        }
        _PermMedia = ImageVector.Builder(
            name = "Filled.PermMedia",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(120f, 840f)
                quadTo(87f, 840f, 63.5f, 816.5f)
                quadTo(40f, 793f, 40f, 760f)
                lineTo(40f, 280f)
                quadTo(40f, 263f, 51.5f, 251.5f)
                quadTo(63f, 240f, 80f, 240f)
                quadTo(97f, 240f, 108.5f, 251.5f)
                quadTo(120f, 263f, 120f, 280f)
                lineTo(120f, 760f)
                quadTo(120f, 760f, 120f, 760f)
                quadTo(120f, 760f, 120f, 760f)
                lineTo(760f, 760f)
                quadTo(777f, 760f, 788.5f, 771.5f)
                quadTo(800f, 783f, 800f, 800f)
                quadTo(800f, 817f, 788.5f, 828.5f)
                quadTo(777f, 840f, 760f, 840f)
                lineTo(120f, 840f)
                close()
                moveTo(280f, 680f)
                quadTo(247f, 680f, 223.5f, 656.5f)
                quadTo(200f, 633f, 200f, 600f)
                lineTo(200f, 160f)
                quadTo(200f, 127f, 223.5f, 103.5f)
                quadTo(247f, 80f, 280f, 80f)
                lineTo(447f, 80f)
                quadTo(463f, 80f, 477.5f, 86f)
                quadTo(492f, 92f, 503f, 103f)
                lineTo(560f, 160f)
                lineTo(840f, 160f)
                quadTo(873f, 160f, 896.5f, 183.5f)
                quadTo(920f, 207f, 920f, 240f)
                lineTo(920f, 600f)
                quadTo(920f, 633f, 896.5f, 656.5f)
                quadTo(873f, 680f, 840f, 680f)
                lineTo(280f, 680f)
                close()
                moveTo(530f, 460f)
                lineTo(484f, 400f)
                quadTo(478f, 392f, 468f, 392f)
                quadTo(458f, 392f, 452f, 400f)
                lineTo(385f, 488f)
                quadTo(377f, 498f, 382.5f, 509f)
                quadTo(388f, 520f, 401f, 520f)
                lineTo(719f, 520f)
                quadTo(732f, 520f, 737.5f, 509f)
                quadTo(743f, 498f, 735f, 488f)
                lineTo(638f, 361f)
                quadTo(632f, 353f, 622f, 353f)
                quadTo(612f, 353f, 606f, 361f)
                lineTo(530f, 460f)
                close()
            }
        }.build()

        return _PermMedia!!
    }

@Suppress("ObjectPropertyName")
private var _PermMedia: ImageVector? = null
