package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.QueueMusic: ImageVector
    get() {
        if (_QueueMusic != null) {
            return _QueueMusic!!
        }
        _QueueMusic = ImageVector.Builder(
            name = "Rounded.QueueMusic",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
            autoMirror = true,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(640f, 800f)
                quadTo(590f, 800f, 555f, 765f)
                quadTo(520f, 730f, 520f, 680f)
                quadTo(520f, 630f, 555f, 595f)
                quadTo(590f, 560f, 640f, 560f)
                quadTo(651f, 560f, 661f, 561.5f)
                quadTo(671f, 563f, 680f, 568f)
                lineTo(680f, 280f)
                quadTo(680f, 263f, 691.5f, 251.5f)
                quadTo(703f, 240f, 720f, 240f)
                lineTo(840f, 240f)
                quadTo(857f, 240f, 868.5f, 251.5f)
                quadTo(880f, 263f, 880f, 280f)
                quadTo(880f, 297f, 868.5f, 308.5f)
                quadTo(857f, 320f, 840f, 320f)
                lineTo(760f, 320f)
                lineTo(760f, 680f)
                quadTo(760f, 730f, 725f, 765f)
                quadTo(690f, 800f, 640f, 800f)
                close()
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
            }
        }.build()

        return _QueueMusic!!
    }

@Suppress("ObjectPropertyName")
private var _QueueMusic: ImageVector? = null
