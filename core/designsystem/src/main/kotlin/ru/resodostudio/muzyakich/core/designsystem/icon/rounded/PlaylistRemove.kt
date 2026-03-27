package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.PlaylistRemove: ImageVector
    get() {
        if (_PlaylistRemove != null) {
            return _PlaylistRemove!!
        }
        _PlaylistRemove = ImageVector.Builder(
            name = "Rounded.PlaylistRemove",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(680f, 776f)
                lineTo(604f, 852f)
                quadTo(593f, 863f, 576f, 863f)
                quadTo(559f, 863f, 548f, 852f)
                quadTo(537f, 841f, 537f, 824f)
                quadTo(537f, 807f, 548f, 796f)
                lineTo(624f, 720f)
                lineTo(548f, 644f)
                quadTo(537f, 633f, 537f, 616f)
                quadTo(537f, 599f, 548f, 588f)
                quadTo(559f, 577f, 576f, 577f)
                quadTo(593f, 577f, 604f, 588f)
                lineTo(680f, 664f)
                lineTo(756f, 588f)
                quadTo(767f, 577f, 784f, 577f)
                quadTo(801f, 577f, 812f, 588f)
                quadTo(823f, 599f, 823f, 616f)
                quadTo(823f, 633f, 812f, 644f)
                lineTo(736f, 720f)
                lineTo(812f, 796f)
                quadTo(823f, 807f, 823f, 824f)
                quadTo(823f, 841f, 812f, 852f)
                quadTo(801f, 863f, 784f, 863f)
                quadTo(767f, 863f, 756f, 852f)
                lineTo(680f, 776f)
                close()
                moveTo(160f, 640f)
                quadTo(143f, 640f, 131.5f, 628.5f)
                quadTo(120f, 617f, 120f, 600f)
                quadTo(120f, 583f, 131.5f, 571.5f)
                quadTo(143f, 560f, 160f, 560f)
                lineTo(360f, 560f)
                quadTo(377f, 560f, 388.5f, 571.5f)
                quadTo(400f, 583f, 400f, 600f)
                quadTo(400f, 617f, 388.5f, 628.5f)
                quadTo(377f, 640f, 360f, 640f)
                lineTo(160f, 640f)
                close()
                moveTo(160f, 480f)
                quadTo(143f, 480f, 131.5f, 468.5f)
                quadTo(120f, 457f, 120f, 440f)
                quadTo(120f, 423f, 131.5f, 411.5f)
                quadTo(143f, 400f, 160f, 400f)
                lineTo(520f, 400f)
                quadTo(537f, 400f, 548.5f, 411.5f)
                quadTo(560f, 423f, 560f, 440f)
                quadTo(560f, 457f, 548.5f, 468.5f)
                quadTo(537f, 480f, 520f, 480f)
                lineTo(160f, 480f)
                close()
                moveTo(160f, 320f)
                quadTo(143f, 320f, 131.5f, 308.5f)
                quadTo(120f, 297f, 120f, 280f)
                quadTo(120f, 263f, 131.5f, 251.5f)
                quadTo(143f, 240f, 160f, 240f)
                lineTo(520f, 240f)
                quadTo(537f, 240f, 548.5f, 251.5f)
                quadTo(560f, 263f, 560f, 280f)
                quadTo(560f, 297f, 548.5f, 308.5f)
                quadTo(537f, 320f, 520f, 320f)
                lineTo(160f, 320f)
                close()
            }
        }.build()

        return _PlaylistRemove!!
    }

@Suppress("ObjectPropertyName")
private var _PlaylistRemove: ImageVector? = null
