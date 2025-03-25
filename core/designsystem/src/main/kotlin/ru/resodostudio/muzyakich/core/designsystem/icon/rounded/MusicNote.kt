package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.MusicNote: ImageVector
    get() {
        if (_MusicNote != null) {
            return _MusicNote!!
        }
        _MusicNote = ImageVector.Builder(
            name = "Rounded.MusicNote",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(400f, 840f)
                quadTo(334f, 840f, 287f, 793f)
                quadTo(240f, 746f, 240f, 680f)
                quadTo(240f, 614f, 287f, 567f)
                quadTo(334f, 520f, 400f, 520f)
                quadTo(423f, 520f, 442.5f, 525.5f)
                quadTo(462f, 531f, 480f, 542f)
                lineTo(480f, 160f)
                quadTo(480f, 143f, 491.5f, 131.5f)
                quadTo(503f, 120f, 520f, 120f)
                lineTo(680f, 120f)
                quadTo(697f, 120f, 708.5f, 131.5f)
                quadTo(720f, 143f, 720f, 160f)
                lineTo(720f, 240f)
                quadTo(720f, 257f, 708.5f, 268.5f)
                quadTo(697f, 280f, 680f, 280f)
                lineTo(560f, 280f)
                lineTo(560f, 680f)
                quadTo(560f, 746f, 513f, 793f)
                quadTo(466f, 840f, 400f, 840f)
                close()
            }
        }.build()

        return _MusicNote!!
    }

@Suppress("ObjectPropertyName")
private var _MusicNote: ImageVector? = null
