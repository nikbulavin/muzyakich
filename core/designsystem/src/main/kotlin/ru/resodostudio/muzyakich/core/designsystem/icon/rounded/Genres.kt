package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.Genres: ImageVector
    get() {
        if (_Genres != null) {
            return _Genres!!
        }
        _Genres = ImageVector.Builder(
            name = "Rounded.Genres",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(485f, 685f)
                quadTo(520f, 650f, 520f, 600f)
                lineTo(520f, 320f)
                lineTo(600f, 320f)
                quadTo(617f, 320f, 628.5f, 308.5f)
                quadTo(640f, 297f, 640f, 280f)
                quadTo(640f, 263f, 628.5f, 251.5f)
                quadTo(617f, 240f, 600f, 240f)
                lineTo(500f, 240f)
                quadTo(483f, 240f, 471.5f, 251.5f)
                quadTo(460f, 263f, 460f, 280f)
                lineTo(460f, 496f)
                quadTo(446f, 488f, 431f, 484f)
                quadTo(416f, 480f, 400f, 480f)
                quadTo(350f, 480f, 315f, 515f)
                quadTo(280f, 550f, 280f, 600f)
                quadTo(280f, 650f, 315f, 685f)
                quadTo(350f, 720f, 400f, 720f)
                quadTo(450f, 720f, 485f, 685f)
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
            }
        }.build()

        return _Genres!!
    }

@Suppress("ObjectPropertyName")
private var _Genres: ImageVector? = null
