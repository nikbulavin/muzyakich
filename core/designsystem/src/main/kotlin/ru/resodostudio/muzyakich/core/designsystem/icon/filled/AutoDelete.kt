package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.AutoDelete: ImageVector
    get() {
        if (_AutoDelete != null) {
            return _AutoDelete!!
        }
        _AutoDelete = ImageVector.Builder(
            name = "Filled.AutoDelete",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(280f, 840f)
                quadTo(247f, 840f, 223.5f, 816.5f)
                quadTo(200f, 793f, 200f, 760f)
                lineTo(200f, 240f)
                lineTo(200f, 240f)
                lineTo(200f, 240f)
                quadTo(183f, 240f, 171.5f, 228.5f)
                quadTo(160f, 217f, 160f, 200f)
                quadTo(160f, 183f, 171.5f, 171.5f)
                quadTo(183f, 160f, 200f, 160f)
                lineTo(360f, 160f)
                lineTo(360f, 160f)
                quadTo(360f, 143f, 371.5f, 131.5f)
                quadTo(383f, 120f, 400f, 120f)
                lineTo(560f, 120f)
                quadTo(577f, 120f, 588.5f, 131.5f)
                quadTo(600f, 143f, 600f, 160f)
                lineTo(600f, 160f)
                lineTo(760f, 160f)
                quadTo(777f, 160f, 788.5f, 171.5f)
                quadTo(800f, 183f, 800f, 200f)
                quadTo(800f, 217f, 788.5f, 228.5f)
                quadTo(777f, 240f, 760f, 240f)
                lineTo(760f, 240f)
                lineTo(760f, 240f)
                lineTo(760f, 366f)
                quadTo(760f, 383f, 746.5f, 394f)
                quadTo(733f, 405f, 715f, 402f)
                quadTo(707f, 401f, 698f, 400.5f)
                quadTo(689f, 400f, 680f, 400f)
                quadTo(660f, 400f, 640f, 402.5f)
                quadTo(620f, 405f, 600f, 411f)
                lineTo(600f, 360f)
                quadTo(600f, 343f, 588.5f, 331.5f)
                quadTo(577f, 320f, 560f, 320f)
                quadTo(543f, 320f, 531.5f, 331.5f)
                quadTo(520f, 343f, 520f, 360f)
                lineTo(520f, 450f)
                quadTo(496f, 467f, 475.5f, 488.5f)
                quadTo(455f, 510f, 440f, 536f)
                lineTo(440f, 360f)
                quadTo(440f, 343f, 428.5f, 331.5f)
                quadTo(417f, 320f, 400f, 320f)
                quadTo(383f, 320f, 371.5f, 331.5f)
                quadTo(360f, 343f, 360f, 360f)
                lineTo(360f, 640f)
                quadTo(360f, 657f, 371.5f, 668.5f)
                quadTo(383f, 680f, 400f, 680f)
                lineTo(400f, 680f)
                quadTo(400f, 709f, 406.5f, 737.5f)
                quadTo(413f, 766f, 424f, 792f)
                quadTo(432f, 809f, 422.5f, 824.5f)
                quadTo(413f, 840f, 396f, 840f)
                lineTo(280f, 840f)
                close()
                moveTo(538.5f, 821.5f)
                quadTo(480f, 763f, 480f, 680f)
                quadTo(480f, 597f, 538.5f, 538.5f)
                quadTo(597f, 480f, 680f, 480f)
                quadTo(763f, 480f, 821.5f, 538.5f)
                quadTo(880f, 597f, 880f, 680f)
                quadTo(880f, 763f, 821.5f, 821.5f)
                quadTo(763f, 880f, 680f, 880f)
                quadTo(597f, 880f, 538.5f, 821.5f)
                close()
                moveTo(700f, 672f)
                lineTo(700f, 580f)
                quadTo(700f, 572f, 694f, 566f)
                quadTo(688f, 560f, 680f, 560f)
                quadTo(672f, 560f, 666f, 566f)
                quadTo(660f, 572f, 660f, 580f)
                lineTo(660f, 671f)
                quadTo(660f, 679f, 663f, 686.5f)
                quadTo(666f, 694f, 672f, 700f)
                lineTo(732f, 760f)
                quadTo(738f, 766f, 746f, 766f)
                quadTo(754f, 766f, 760f, 760f)
                quadTo(766f, 754f, 766f, 746f)
                quadTo(766f, 738f, 760f, 732f)
                lineTo(700f, 672f)
                close()
            }
        }.build()

        return _AutoDelete!!
    }

@Suppress("ObjectPropertyName")
private var _AutoDelete: ImageVector? = null
