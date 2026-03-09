package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.Policy: ImageVector
    get() {
        if (_Policy != null) {
            return _Policy!!
        }
        _Policy = ImageVector.Builder(
            name = "Filled.Policy",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(467f, 875f)
                quadTo(461f, 874f, 455f, 872f)
                quadTo(320f, 827f, 240f, 705.5f)
                quadTo(160f, 584f, 160f, 444f)
                lineTo(160f, 255f)
                quadTo(160f, 230f, 174.5f, 210f)
                quadTo(189f, 190f, 212f, 181f)
                lineTo(452f, 91f)
                quadTo(466f, 86f, 480f, 86f)
                quadTo(494f, 86f, 508f, 91f)
                lineTo(748f, 181f)
                quadTo(771f, 190f, 785.5f, 210f)
                quadTo(800f, 230f, 800f, 255f)
                lineTo(800f, 444f)
                quadTo(800f, 507f, 783.5f, 566.5f)
                quadTo(767f, 626f, 736f, 680f)
                lineTo(618f, 562f)
                quadTo(629f, 543f, 634.5f, 522.5f)
                quadTo(640f, 502f, 640f, 480f)
                quadTo(640f, 414f, 593f, 367f)
                quadTo(546f, 320f, 480f, 320f)
                quadTo(414f, 320f, 367f, 367f)
                quadTo(320f, 414f, 320f, 480f)
                quadTo(320f, 546f, 367f, 593f)
                quadTo(414f, 640f, 480f, 640f)
                quadTo(501f, 640f, 521.5f, 634.5f)
                quadTo(542f, 629f, 560f, 618f)
                lineTo(689f, 746f)
                quadTo(651f, 791f, 605.5f, 822.5f)
                quadTo(560f, 854f, 505f, 872f)
                quadTo(499f, 874f, 493f, 875f)
                quadTo(487f, 876f, 480f, 876f)
                quadTo(473f, 876f, 467f, 875f)
                close()
                moveTo(423.5f, 536.5f)
                quadTo(400f, 513f, 400f, 480f)
                quadTo(400f, 447f, 423.5f, 423.5f)
                quadTo(447f, 400f, 480f, 400f)
                quadTo(513f, 400f, 536.5f, 423.5f)
                quadTo(560f, 447f, 560f, 480f)
                quadTo(560f, 513f, 536.5f, 536.5f)
                quadTo(513f, 560f, 480f, 560f)
                quadTo(447f, 560f, 423.5f, 536.5f)
                close()
            }
        }.build()

        return _Policy!!
    }

@Suppress("ObjectPropertyName")
private var _Policy: ImageVector? = null
