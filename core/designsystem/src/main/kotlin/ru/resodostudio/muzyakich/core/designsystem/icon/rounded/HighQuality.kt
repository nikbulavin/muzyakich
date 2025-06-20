package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.HighQuality: ImageVector
    get() {
        if (_HighQuality != null) {
            return _HighQuality!!
        }
        _HighQuality = ImageVector.Builder(
            name = "Rounded.HighQuality",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(590f, 600f)
                lineTo(590f, 630f)
                quadTo(590f, 643f, 598.5f, 651.5f)
                quadTo(607f, 660f, 620f, 660f)
                quadTo(633f, 660f, 641.5f, 651.5f)
                quadTo(650f, 643f, 650f, 630f)
                lineTo(650f, 600f)
                lineTo(680f, 600f)
                quadTo(697f, 600f, 708.5f, 588.5f)
                quadTo(720f, 577f, 720f, 560f)
                lineTo(720f, 400f)
                quadTo(720f, 383f, 708.5f, 371.5f)
                quadTo(697f, 360f, 680f, 360f)
                lineTo(560f, 360f)
                quadTo(543f, 360f, 531.5f, 371.5f)
                quadTo(520f, 383f, 520f, 400f)
                lineTo(520f, 560f)
                quadTo(520f, 577f, 531.5f, 588.5f)
                quadTo(543f, 600f, 560f, 600f)
                lineTo(590f, 600f)
                close()
                moveTo(300f, 520f)
                lineTo(380f, 520f)
                lineTo(380f, 570f)
                quadTo(380f, 583f, 388.5f, 591.5f)
                quadTo(397f, 600f, 410f, 600f)
                quadTo(423f, 600f, 431.5f, 591.5f)
                quadTo(440f, 583f, 440f, 570f)
                lineTo(440f, 390f)
                quadTo(440f, 377f, 431.5f, 368.5f)
                quadTo(423f, 360f, 410f, 360f)
                quadTo(397f, 360f, 388.5f, 368.5f)
                quadTo(380f, 377f, 380f, 390f)
                lineTo(380f, 460f)
                lineTo(300f, 460f)
                lineTo(300f, 390f)
                quadTo(300f, 377f, 291.5f, 368.5f)
                quadTo(283f, 360f, 270f, 360f)
                quadTo(257f, 360f, 248.5f, 368.5f)
                quadTo(240f, 377f, 240f, 390f)
                lineTo(240f, 570f)
                quadTo(240f, 583f, 248.5f, 591.5f)
                quadTo(257f, 600f, 270f, 600f)
                quadTo(283f, 600f, 291.5f, 591.5f)
                quadTo(300f, 583f, 300f, 570f)
                lineTo(300f, 520f)
                close()
                moveTo(580f, 540f)
                quadTo(580f, 540f, 580f, 540f)
                quadTo(580f, 540f, 580f, 540f)
                lineTo(580f, 420f)
                quadTo(580f, 420f, 580f, 420f)
                quadTo(580f, 420f, 580f, 420f)
                lineTo(660f, 420f)
                quadTo(660f, 420f, 660f, 420f)
                quadTo(660f, 420f, 660f, 420f)
                lineTo(660f, 540f)
                quadTo(660f, 540f, 660f, 540f)
                quadTo(660f, 540f, 660f, 540f)
                lineTo(580f, 540f)
                close()
                moveTo(160f, 800f)
                quadTo(127f, 800f, 103.5f, 776.5f)
                quadTo(80f, 753f, 80f, 720f)
                lineTo(80f, 240f)
                quadTo(80f, 207f, 103.5f, 183.5f)
                quadTo(127f, 160f, 160f, 160f)
                lineTo(800f, 160f)
                quadTo(833f, 160f, 856.5f, 183.5f)
                quadTo(880f, 207f, 880f, 240f)
                lineTo(880f, 720f)
                quadTo(880f, 753f, 856.5f, 776.5f)
                quadTo(833f, 800f, 800f, 800f)
                lineTo(160f, 800f)
                close()
            }
        }.build()

        return _HighQuality!!
    }

@Suppress("ObjectPropertyName")
private var _HighQuality: ImageVector? = null
