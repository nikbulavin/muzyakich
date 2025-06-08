package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.Shuffle: ImageVector
    get() {
        if (_Shuffle != null) {
            return _Shuffle!!
        }
        _Shuffle = ImageVector.Builder(
            name = "Rounded.Shuffle",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(600f, 800f)
                quadTo(583f, 800f, 571.5f, 788.5f)
                quadTo(560f, 777f, 560f, 760f)
                quadTo(560f, 743f, 571.5f, 731.5f)
                quadTo(583f, 720f, 600f, 720f)
                lineTo(664f, 720f)
                lineTo(565f, 621f)
                quadTo(553f, 609f, 553.5f, 592.5f)
                quadTo(554f, 576f, 566f, 564f)
                quadTo(578f, 552f, 594.5f, 552f)
                quadTo(611f, 552f, 623f, 564f)
                lineTo(720f, 662f)
                lineTo(720f, 600f)
                quadTo(720f, 583f, 731.5f, 571.5f)
                quadTo(743f, 560f, 760f, 560f)
                quadTo(777f, 560f, 788.5f, 571.5f)
                quadTo(800f, 583f, 800f, 600f)
                lineTo(800f, 760f)
                quadTo(800f, 777f, 788.5f, 788.5f)
                quadTo(777f, 800f, 760f, 800f)
                lineTo(600f, 800f)
                close()
                moveTo(172f, 788f)
                quadTo(161f, 777f, 161f, 760f)
                quadTo(161f, 743f, 172f, 732f)
                lineTo(664f, 240f)
                lineTo(600f, 240f)
                quadTo(583f, 240f, 571.5f, 228.5f)
                quadTo(560f, 217f, 560f, 200f)
                quadTo(560f, 183f, 571.5f, 171.5f)
                quadTo(583f, 160f, 600f, 160f)
                lineTo(760f, 160f)
                quadTo(777f, 160f, 788.5f, 171.5f)
                quadTo(800f, 183f, 800f, 200f)
                lineTo(800f, 360f)
                quadTo(800f, 377f, 788.5f, 388.5f)
                quadTo(777f, 400f, 760f, 400f)
                quadTo(743f, 400f, 731.5f, 388.5f)
                quadTo(720f, 377f, 720f, 360f)
                lineTo(720f, 296f)
                lineTo(228f, 788f)
                quadTo(217f, 799f, 200f, 799f)
                quadTo(183f, 799f, 172f, 788f)
                close()
                moveTo(171f, 228f)
                quadTo(160f, 217f, 160f, 200f)
                quadTo(160f, 183f, 171f, 172f)
                quadTo(182f, 161f, 198.5f, 161f)
                quadTo(215f, 161f, 227f, 172f)
                lineTo(395f, 339f)
                quadTo(406f, 350f, 406.5f, 366.5f)
                quadTo(407f, 383f, 395f, 395f)
                quadTo(384f, 406f, 367f, 406f)
                quadTo(350f, 406f, 339f, 395f)
                lineTo(171f, 228f)
                close()
            }
        }.build()

        return _Shuffle!!
    }

@Suppress("ObjectPropertyName")
private var _Shuffle: ImageVector? = null
