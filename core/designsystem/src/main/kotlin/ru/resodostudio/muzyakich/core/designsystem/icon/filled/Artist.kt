package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.Artist: ImageVector
    get() {
        if (_Artist != null) {
            return _Artist!!
        }
        _Artist = ImageVector.Builder(
            name = "Filled.Artist",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(700f, 800f)
                quadTo(658f, 800f, 629f, 771f)
                quadTo(600f, 742f, 600f, 700f)
                quadTo(600f, 658f, 629f, 629f)
                quadTo(658f, 600f, 700f, 600f)
                quadTo(708f, 600f, 718f, 601.5f)
                quadTo(728f, 603f, 740f, 608f)
                lineTo(740f, 440f)
                quadTo(740f, 423f, 751.5f, 411.5f)
                quadTo(763f, 400f, 780f, 400f)
                lineTo(840f, 400f)
                quadTo(857f, 400f, 868.5f, 411.5f)
                quadTo(880f, 423f, 880f, 440f)
                quadTo(880f, 457f, 868.5f, 468.5f)
                quadTo(857f, 480f, 840f, 480f)
                lineTo(800f, 480f)
                lineTo(800f, 700f)
                quadTo(800f, 742f, 771f, 771f)
                quadTo(742f, 800f, 700f, 800f)
                close()
                moveTo(440f, 480f)
                quadTo(374f, 480f, 327f, 433f)
                quadTo(280f, 386f, 280f, 320f)
                quadTo(280f, 254f, 327f, 207f)
                quadTo(374f, 160f, 440f, 160f)
                quadTo(506f, 160f, 553f, 207f)
                quadTo(600f, 254f, 600f, 320f)
                quadTo(600f, 386f, 553f, 433f)
                quadTo(506f, 480f, 440f, 480f)
                close()
                moveTo(160f, 800f)
                quadTo(143f, 800f, 131.5f, 788.5f)
                quadTo(120f, 777f, 120f, 760f)
                lineTo(120f, 688f)
                quadTo(120f, 653f, 137.5f, 625f)
                quadTo(155f, 597f, 184f, 582f)
                quadTo(246f, 551f, 310f, 535.5f)
                quadTo(374f, 520f, 440f, 520f)
                quadTo(468f, 520f, 495.5f, 523f)
                quadTo(523f, 526f, 551f, 532f)
                quadTo(568f, 536f, 572.5f, 553f)
                quadTo(577f, 570f, 563f, 584f)
                quadTo(542f, 609f, 531.5f, 638.5f)
                quadTo(521f, 668f, 521f, 700f)
                quadTo(521f, 713f, 522.5f, 725.5f)
                quadTo(524f, 738f, 528f, 751f)
                quadTo(533f, 769f, 523.5f, 784.5f)
                quadTo(514f, 800f, 497f, 800f)
                lineTo(160f, 800f)
                close()
            }
        }.build()

        return _Artist!!
    }

@Suppress("ObjectPropertyName")
private var _Artist: ImageVector? = null
