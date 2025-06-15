package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.Star: ImageVector
    get() {
        if (_Star != null) {
            return _Star!!
        }
        _Star = ImageVector.Builder(
            name = "Filled.Star",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(480f, 691f)
                lineTo(314f, 791f)
                quadTo(303f, 798f, 291f, 797f)
                quadTo(279f, 796f, 270f, 789f)
                quadTo(261f, 782f, 256f, 771.5f)
                quadTo(251f, 761f, 254f, 748f)
                lineTo(298f, 559f)
                lineTo(151f, 432f)
                quadTo(141f, 423f, 138.5f, 411.5f)
                quadTo(136f, 400f, 140f, 389f)
                quadTo(144f, 378f, 152f, 371f)
                quadTo(160f, 364f, 174f, 362f)
                lineTo(368f, 345f)
                lineTo(443f, 167f)
                quadTo(448f, 155f, 458.5f, 149f)
                quadTo(469f, 143f, 480f, 143f)
                quadTo(491f, 143f, 501.5f, 149f)
                quadTo(512f, 155f, 517f, 167f)
                lineTo(592f, 345f)
                lineTo(786f, 362f)
                quadTo(800f, 364f, 808f, 371f)
                quadTo(816f, 378f, 820f, 389f)
                quadTo(824f, 400f, 821.5f, 411.5f)
                quadTo(819f, 423f, 809f, 432f)
                lineTo(662f, 559f)
                lineTo(706f, 748f)
                quadTo(709f, 761f, 704f, 771.5f)
                quadTo(699f, 782f, 690f, 789f)
                quadTo(681f, 796f, 669f, 797f)
                quadTo(657f, 798f, 646f, 791f)
                lineTo(480f, 691f)
                close()
            }
        }.build()

        return _Star!!
    }

@Suppress("ObjectPropertyName")
private var _Star: ImageVector? = null
