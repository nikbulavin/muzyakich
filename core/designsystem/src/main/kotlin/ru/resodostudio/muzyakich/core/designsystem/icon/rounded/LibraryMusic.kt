package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.LibraryMusic: ImageVector
    get() {
        if (_LibraryMusic != null) {
            return _LibraryMusic!!
        }
        _LibraryMusic = ImageVector.Builder(
            name = "Rounded.LibraryMusic",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(500f, 600f)
                quadTo(542f, 600f, 571f, 571f)
                quadTo(600f, 542f, 600f, 500f)
                lineTo(600f, 280f)
                lineTo(680f, 280f)
                quadTo(697f, 280f, 708.5f, 268.5f)
                quadTo(720f, 257f, 720f, 240f)
                quadTo(720f, 223f, 708.5f, 211.5f)
                quadTo(697f, 200f, 680f, 200f)
                lineTo(600f, 200f)
                quadTo(583f, 200f, 571.5f, 211.5f)
                quadTo(560f, 223f, 560f, 240f)
                lineTo(560f, 420f)
                quadTo(547f, 410f, 532f, 405f)
                quadTo(517f, 400f, 500f, 400f)
                quadTo(458f, 400f, 429f, 429f)
                quadTo(400f, 458f, 400f, 500f)
                quadTo(400f, 542f, 429f, 571f)
                quadTo(458f, 600f, 500f, 600f)
                close()
                moveTo(320f, 720f)
                quadTo(287f, 720f, 263.5f, 696.5f)
                quadTo(240f, 673f, 240f, 640f)
                lineTo(240f, 160f)
                quadTo(240f, 127f, 263.5f, 103.5f)
                quadTo(287f, 80f, 320f, 80f)
                lineTo(800f, 80f)
                quadTo(833f, 80f, 856.5f, 103.5f)
                quadTo(880f, 127f, 880f, 160f)
                lineTo(880f, 640f)
                quadTo(880f, 673f, 856.5f, 696.5f)
                quadTo(833f, 720f, 800f, 720f)
                lineTo(320f, 720f)
                close()
                moveTo(320f, 640f)
                lineTo(800f, 640f)
                quadTo(800f, 640f, 800f, 640f)
                quadTo(800f, 640f, 800f, 640f)
                lineTo(800f, 160f)
                quadTo(800f, 160f, 800f, 160f)
                quadTo(800f, 160f, 800f, 160f)
                lineTo(320f, 160f)
                quadTo(320f, 160f, 320f, 160f)
                quadTo(320f, 160f, 320f, 160f)
                lineTo(320f, 640f)
                quadTo(320f, 640f, 320f, 640f)
                quadTo(320f, 640f, 320f, 640f)
                close()
                moveTo(160f, 880f)
                quadTo(127f, 880f, 103.5f, 856.5f)
                quadTo(80f, 833f, 80f, 800f)
                lineTo(80f, 280f)
                quadTo(80f, 263f, 91.5f, 251.5f)
                quadTo(103f, 240f, 120f, 240f)
                quadTo(137f, 240f, 148.5f, 251.5f)
                quadTo(160f, 263f, 160f, 280f)
                lineTo(160f, 800f)
                quadTo(160f, 800f, 160f, 800f)
                quadTo(160f, 800f, 160f, 800f)
                lineTo(680f, 800f)
                quadTo(697f, 800f, 708.5f, 811.5f)
                quadTo(720f, 823f, 720f, 840f)
                quadTo(720f, 857f, 708.5f, 868.5f)
                quadTo(697f, 880f, 680f, 880f)
                lineTo(160f, 880f)
                close()
                moveTo(320f, 160f)
                lineTo(320f, 160f)
                quadTo(320f, 160f, 320f, 160f)
                quadTo(320f, 160f, 320f, 160f)
                lineTo(320f, 640f)
                quadTo(320f, 640f, 320f, 640f)
                quadTo(320f, 640f, 320f, 640f)
                lineTo(320f, 640f)
                quadTo(320f, 640f, 320f, 640f)
                quadTo(320f, 640f, 320f, 640f)
                lineTo(320f, 160f)
                quadTo(320f, 160f, 320f, 160f)
                quadTo(320f, 160f, 320f, 160f)
                close()
            }
        }.build()

        return _LibraryMusic!!
    }

@Suppress("ObjectPropertyName")
private var _LibraryMusic: ImageVector? = null
