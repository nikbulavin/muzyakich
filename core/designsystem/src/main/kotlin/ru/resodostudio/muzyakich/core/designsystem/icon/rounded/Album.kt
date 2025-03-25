package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.Album: ImageVector
    get() {
        if (_Album != null) {
            return _Album!!
        }
        _Album = ImageVector.Builder(
            name = "Rounded.Album",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(480f, 660f)
                quadTo(555f, 660f, 607.5f, 607.5f)
                quadTo(660f, 555f, 660f, 480f)
                quadTo(660f, 405f, 607.5f, 352.5f)
                quadTo(555f, 300f, 480f, 300f)
                quadTo(405f, 300f, 352.5f, 352.5f)
                quadTo(300f, 405f, 300f, 480f)
                quadTo(300f, 555f, 352.5f, 607.5f)
                quadTo(405f, 660f, 480f, 660f)
                close()
                moveTo(480f, 520f)
                quadTo(463f, 520f, 451.5f, 508.5f)
                quadTo(440f, 497f, 440f, 480f)
                quadTo(440f, 463f, 451.5f, 451.5f)
                quadTo(463f, 440f, 480f, 440f)
                quadTo(497f, 440f, 508.5f, 451.5f)
                quadTo(520f, 463f, 520f, 480f)
                quadTo(520f, 497f, 508.5f, 508.5f)
                quadTo(497f, 520f, 480f, 520f)
                close()
                moveTo(480f, 880f)
                quadTo(397f, 880f, 324f, 848.5f)
                quadTo(251f, 817f, 197f, 763f)
                quadTo(143f, 709f, 111.5f, 636f)
                quadTo(80f, 563f, 80f, 480f)
                quadTo(80f, 397f, 111.5f, 324f)
                quadTo(143f, 251f, 197f, 197f)
                quadTo(251f, 143f, 324f, 111.5f)
                quadTo(397f, 80f, 480f, 80f)
                quadTo(563f, 80f, 636f, 111.5f)
                quadTo(709f, 143f, 763f, 197f)
                quadTo(817f, 251f, 848.5f, 324f)
                quadTo(880f, 397f, 880f, 480f)
                quadTo(880f, 563f, 848.5f, 636f)
                quadTo(817f, 709f, 763f, 763f)
                quadTo(709f, 817f, 636f, 848.5f)
                quadTo(563f, 880f, 480f, 880f)
                close()
                moveTo(480f, 800f)
                quadTo(614f, 800f, 707f, 707f)
                quadTo(800f, 614f, 800f, 480f)
                quadTo(800f, 346f, 707f, 253f)
                quadTo(614f, 160f, 480f, 160f)
                quadTo(346f, 160f, 253f, 253f)
                quadTo(160f, 346f, 160f, 480f)
                quadTo(160f, 614f, 253f, 707f)
                quadTo(346f, 800f, 480f, 800f)
                close()
                moveTo(480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                close()
            }
        }.build()

        return _Album!!
    }

@Suppress("ObjectPropertyName")
private var _Album: ImageVector? = null
