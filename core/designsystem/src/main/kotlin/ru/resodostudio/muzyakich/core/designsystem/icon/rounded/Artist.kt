package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.Artist: ImageVector
    get() {
        if (_Artist != null) {
            return _Artist!!
        }
        _Artist = ImageVector.Builder(
            name = "Rounded.Artist",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
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
                moveTo(160f, 800f)
                quadTo(143f, 800f, 131.5f, 788.5f)
                quadTo(120f, 777f, 120f, 760f)
                lineTo(120f, 688f)
                quadTo(120f, 653f, 137.5f, 625f)
                quadTo(155f, 597f, 184f, 582f)
                quadTo(246f, 551f, 310f, 535.5f)
                quadTo(374f, 520f, 440f, 520f)
                quadTo(463f, 520f, 487f, 521.5f)
                quadTo(511f, 523f, 534f, 528f)
                quadTo(555f, 532f, 563f, 546f)
                quadTo(571f, 560f, 569f, 575f)
                quadTo(567f, 590f, 555f, 601f)
                quadTo(543f, 612f, 524f, 608f)
                quadTo(503f, 604f, 482.5f, 602f)
                quadTo(462f, 600f, 440f, 600f)
                quadTo(383f, 600f, 328f, 614f)
                quadTo(273f, 628f, 220f, 654f)
                quadTo(211f, 659f, 205.5f, 668f)
                quadTo(200f, 677f, 200f, 688f)
                lineTo(200f, 720f)
                lineTo(493f, 720f)
                quadTo(513f, 720f, 523f, 732.5f)
                quadTo(533f, 745f, 533f, 760f)
                quadTo(533f, 775f, 523f, 787.5f)
                quadTo(513f, 800f, 493f, 800f)
                lineTo(160f, 800f)
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
                moveTo(440f, 400f)
                quadTo(473f, 400f, 496.5f, 376.5f)
                quadTo(520f, 353f, 520f, 320f)
                quadTo(520f, 287f, 496.5f, 263.5f)
                quadTo(473f, 240f, 440f, 240f)
                quadTo(407f, 240f, 383.5f, 263.5f)
                quadTo(360f, 287f, 360f, 320f)
                quadTo(360f, 353f, 383.5f, 376.5f)
                quadTo(407f, 400f, 440f, 400f)
                close()
                moveTo(440f, 320f)
                quadTo(440f, 320f, 440f, 320f)
                quadTo(440f, 320f, 440f, 320f)
                quadTo(440f, 320f, 440f, 320f)
                quadTo(440f, 320f, 440f, 320f)
                quadTo(440f, 320f, 440f, 320f)
                quadTo(440f, 320f, 440f, 320f)
                quadTo(440f, 320f, 440f, 320f)
                quadTo(440f, 320f, 440f, 320f)
                close()
                moveTo(440f, 720f)
                lineTo(440f, 720f)
                lineTo(440f, 720f)
                quadTo(440f, 720f, 440f, 720f)
                quadTo(440f, 720f, 440f, 720f)
                quadTo(440f, 720f, 440f, 720f)
                quadTo(440f, 720f, 440f, 720f)
                quadTo(440f, 720f, 440f, 720f)
                quadTo(440f, 720f, 440f, 720f)
                quadTo(440f, 720f, 440f, 720f)
                quadTo(440f, 720f, 440f, 720f)
                close()
            }
        }.build()

        return _Artist!!
    }

@Suppress("ObjectPropertyName")
private var _Artist: ImageVector? = null
