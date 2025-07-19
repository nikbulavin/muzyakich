package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.PlaylistPlay: ImageVector
    get() {
        if (_PlaylistPlay != null) {
            return _PlaylistPlay!!
        }
        _PlaylistPlay = ImageVector.Builder(
            name = "Filled.PlaylistPlay",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(160f, 640f)
                quadTo(143f, 640f, 131.5f, 628.5f)
                quadTo(120f, 617f, 120f, 600f)
                quadTo(120f, 583f, 131.5f, 571.5f)
                quadTo(143f, 560f, 160f, 560f)
                lineTo(400f, 560f)
                quadTo(417f, 560f, 428.5f, 571.5f)
                quadTo(440f, 583f, 440f, 600f)
                quadTo(440f, 617f, 428.5f, 628.5f)
                quadTo(417f, 640f, 400f, 640f)
                lineTo(160f, 640f)
                close()
                moveTo(160f, 480f)
                quadTo(143f, 480f, 131.5f, 468.5f)
                quadTo(120f, 457f, 120f, 440f)
                quadTo(120f, 423f, 131.5f, 411.5f)
                quadTo(143f, 400f, 160f, 400f)
                lineTo(560f, 400f)
                quadTo(577f, 400f, 588.5f, 411.5f)
                quadTo(600f, 423f, 600f, 440f)
                quadTo(600f, 457f, 588.5f, 468.5f)
                quadTo(577f, 480f, 560f, 480f)
                lineTo(160f, 480f)
                close()
                moveTo(160f, 320f)
                quadTo(143f, 320f, 131.5f, 308.5f)
                quadTo(120f, 297f, 120f, 280f)
                quadTo(120f, 263f, 131.5f, 251.5f)
                quadTo(143f, 240f, 160f, 240f)
                lineTo(560f, 240f)
                quadTo(577f, 240f, 588.5f, 251.5f)
                quadTo(600f, 263f, 600f, 280f)
                quadTo(600f, 297f, 588.5f, 308.5f)
                quadTo(577f, 320f, 560f, 320f)
                lineTo(160f, 320f)
                close()
                moveTo(671f, 819f)
                quadTo(666f, 822f, 661f, 822f)
                quadTo(656f, 822f, 651f, 820f)
                quadTo(646f, 818f, 643f, 813.5f)
                quadTo(640f, 809f, 640f, 803f)
                lineTo(640f, 557f)
                quadTo(640f, 551f, 643f, 546.5f)
                quadTo(646f, 542f, 651f, 540f)
                quadTo(656f, 538f, 661f, 538f)
                quadTo(666f, 538f, 671f, 541f)
                lineTo(855f, 663f)
                quadTo(860f, 666f, 862f, 670.5f)
                quadTo(864f, 675f, 864f, 680f)
                quadTo(864f, 685f, 862f, 689.5f)
                quadTo(860f, 694f, 855f, 697f)
                lineTo(671f, 819f)
                close()
            }
        }.build()

        return _PlaylistPlay!!
    }

@Suppress("ObjectPropertyName")
private var _PlaylistPlay: ImageVector? = null
