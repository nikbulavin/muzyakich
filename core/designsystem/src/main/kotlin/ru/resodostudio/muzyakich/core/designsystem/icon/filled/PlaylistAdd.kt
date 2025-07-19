package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.PlaylistAdd: ImageVector
    get() {
        if (_PlaylistAdd != null) {
            return _PlaylistAdd!!
        }
        _PlaylistAdd = ImageVector.Builder(
            name = "Filled.PlaylistAdd",
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
                lineTo(360f, 560f)
                quadTo(377f, 560f, 388.5f, 571.5f)
                quadTo(400f, 583f, 400f, 600f)
                quadTo(400f, 617f, 388.5f, 628.5f)
                quadTo(377f, 640f, 360f, 640f)
                lineTo(160f, 640f)
                close()
                moveTo(160f, 480f)
                quadTo(143f, 480f, 131.5f, 468.5f)
                quadTo(120f, 457f, 120f, 440f)
                quadTo(120f, 423f, 131.5f, 411.5f)
                quadTo(143f, 400f, 160f, 400f)
                lineTo(520f, 400f)
                quadTo(537f, 400f, 548.5f, 411.5f)
                quadTo(560f, 423f, 560f, 440f)
                quadTo(560f, 457f, 548.5f, 468.5f)
                quadTo(537f, 480f, 520f, 480f)
                lineTo(160f, 480f)
                close()
                moveTo(160f, 320f)
                quadTo(143f, 320f, 131.5f, 308.5f)
                quadTo(120f, 297f, 120f, 280f)
                quadTo(120f, 263f, 131.5f, 251.5f)
                quadTo(143f, 240f, 160f, 240f)
                lineTo(520f, 240f)
                quadTo(537f, 240f, 548.5f, 251.5f)
                quadTo(560f, 263f, 560f, 280f)
                quadTo(560f, 297f, 548.5f, 308.5f)
                quadTo(537f, 320f, 520f, 320f)
                lineTo(160f, 320f)
                close()
                moveTo(680f, 800f)
                quadTo(663f, 800f, 651.5f, 788.5f)
                quadTo(640f, 777f, 640f, 760f)
                lineTo(640f, 640f)
                lineTo(520f, 640f)
                quadTo(503f, 640f, 491.5f, 628.5f)
                quadTo(480f, 617f, 480f, 600f)
                quadTo(480f, 583f, 491.5f, 571.5f)
                quadTo(503f, 560f, 520f, 560f)
                lineTo(640f, 560f)
                lineTo(640f, 440f)
                quadTo(640f, 423f, 651.5f, 411.5f)
                quadTo(663f, 400f, 680f, 400f)
                quadTo(697f, 400f, 708.5f, 411.5f)
                quadTo(720f, 423f, 720f, 440f)
                lineTo(720f, 560f)
                lineTo(840f, 560f)
                quadTo(857f, 560f, 868.5f, 571.5f)
                quadTo(880f, 583f, 880f, 600f)
                quadTo(880f, 617f, 868.5f, 628.5f)
                quadTo(857f, 640f, 840f, 640f)
                lineTo(720f, 640f)
                lineTo(720f, 760f)
                quadTo(720f, 777f, 708.5f, 788.5f)
                quadTo(697f, 800f, 680f, 800f)
                close()
            }
        }.build()

        return _PlaylistAdd!!
    }

@Suppress("ObjectPropertyName")
private var _PlaylistAdd: ImageVector? = null
