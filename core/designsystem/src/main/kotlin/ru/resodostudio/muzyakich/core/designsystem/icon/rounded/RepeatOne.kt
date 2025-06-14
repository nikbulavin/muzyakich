package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.RepeatOne: ImageVector
    get() {
        if (_RepeatOne != null) {
            return _RepeatOne!!
        }
        _RepeatOne = ImageVector.Builder(
            name = "Rounded.RepeatOne",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(460f, 420f)
                lineTo(430f, 420f)
                quadTo(417f, 420f, 408.5f, 411.5f)
                quadTo(400f, 403f, 400f, 390f)
                quadTo(400f, 377f, 408.5f, 368.5f)
                quadTo(417f, 360f, 430f, 360f)
                lineTo(480f, 360f)
                quadTo(497f, 360f, 508.5f, 371.5f)
                quadTo(520f, 383f, 520f, 400f)
                lineTo(520f, 570f)
                quadTo(520f, 583f, 511.5f, 591.5f)
                quadTo(503f, 600f, 490f, 600f)
                quadTo(477f, 600f, 468.5f, 591.5f)
                quadTo(460f, 583f, 460f, 570f)
                lineTo(460f, 420f)
                close()
                moveTo(274f, 760f)
                lineTo(308f, 794f)
                quadTo(320f, 806f, 319.5f, 822f)
                quadTo(319f, 838f, 308f, 850f)
                quadTo(296f, 862f, 279.5f, 862.5f)
                quadTo(263f, 863f, 251f, 851f)
                lineTo(148f, 748f)
                quadTo(142f, 742f, 139.5f, 735f)
                quadTo(137f, 728f, 137f, 720f)
                quadTo(137f, 712f, 139.5f, 705f)
                quadTo(142f, 698f, 148f, 692f)
                lineTo(251f, 589f)
                quadTo(263f, 577f, 279.5f, 577.5f)
                quadTo(296f, 578f, 308f, 590f)
                quadTo(319f, 602f, 319.5f, 618f)
                quadTo(320f, 634f, 308f, 646f)
                lineTo(274f, 680f)
                lineTo(680f, 680f)
                quadTo(680f, 680f, 680f, 680f)
                quadTo(680f, 680f, 680f, 680f)
                lineTo(680f, 560f)
                quadTo(680f, 543f, 691.5f, 531.5f)
                quadTo(703f, 520f, 720f, 520f)
                quadTo(737f, 520f, 748.5f, 531.5f)
                quadTo(760f, 543f, 760f, 560f)
                lineTo(760f, 680f)
                quadTo(760f, 713f, 736.5f, 736.5f)
                quadTo(713f, 760f, 680f, 760f)
                lineTo(274f, 760f)
                close()
                moveTo(686f, 280f)
                lineTo(280f, 280f)
                quadTo(280f, 280f, 280f, 280f)
                quadTo(280f, 280f, 280f, 280f)
                lineTo(280f, 400f)
                quadTo(280f, 417f, 268.5f, 428.5f)
                quadTo(257f, 440f, 240f, 440f)
                quadTo(223f, 440f, 211.5f, 428.5f)
                quadTo(200f, 417f, 200f, 400f)
                lineTo(200f, 280f)
                quadTo(200f, 247f, 223.5f, 223.5f)
                quadTo(247f, 200f, 280f, 200f)
                lineTo(686f, 200f)
                lineTo(652f, 166f)
                quadTo(640f, 154f, 640.5f, 138f)
                quadTo(641f, 122f, 652f, 110f)
                quadTo(664f, 98f, 680.5f, 97.5f)
                quadTo(697f, 97f, 709f, 109f)
                lineTo(812f, 212f)
                quadTo(818f, 218f, 820.5f, 225f)
                quadTo(823f, 232f, 823f, 240f)
                quadTo(823f, 248f, 820.5f, 255f)
                quadTo(818f, 262f, 812f, 268f)
                lineTo(709f, 371f)
                quadTo(697f, 383f, 680.5f, 382.5f)
                quadTo(664f, 382f, 652f, 370f)
                quadTo(641f, 358f, 640.5f, 342f)
                quadTo(640f, 326f, 652f, 314f)
                lineTo(686f, 280f)
                close()
            }
        }.build()

        return _RepeatOne!!
    }

@Suppress("ObjectPropertyName")
private var _RepeatOne: ImageVector? = null
