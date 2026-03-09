package ru.resodostudio.muzyakich.core.designsystem.icon.rounded

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Rounded.DarkMode: ImageVector
    get() {
        if (_DarkMode != null) {
            return _DarkMode!!
        }
        _DarkMode = ImageVector.Builder(
            name = "Rounded.DarkMode",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(480f, 840f)
                quadTo(329f, 840f, 224.5f, 735.5f)
                quadTo(120f, 631f, 120f, 480f)
                quadTo(120f, 342f, 210f, 240.5f)
                quadTo(300f, 139f, 440f, 122f)
                quadTo(453f, 120f, 463f, 125.5f)
                quadTo(473f, 131f, 479f, 140f)
                quadTo(485f, 149f, 485.5f, 161f)
                quadTo(486f, 173f, 478f, 184f)
                quadTo(461f, 210f, 452.5f, 239f)
                quadTo(444f, 268f, 444f, 300f)
                quadTo(444f, 390f, 507f, 453f)
                quadTo(570f, 516f, 660f, 516f)
                quadTo(691f, 516f, 721.5f, 507f)
                quadTo(752f, 498f, 776f, 482f)
                quadTo(787f, 475f, 798.5f, 475.5f)
                quadTo(810f, 476f, 819f, 481f)
                quadTo(829f, 486f, 834.5f, 496f)
                quadTo(840f, 506f, 838f, 520f)
                quadTo(824f, 658f, 720.5f, 749f)
                quadTo(617f, 840f, 480f, 840f)
                close()
                moveTo(480f, 760f)
                quadTo(568f, 760f, 638f, 711.5f)
                quadTo(708f, 663f, 740f, 585f)
                quadTo(720f, 590f, 700f, 593f)
                quadTo(680f, 596f, 660f, 596f)
                quadTo(537f, 596f, 450.5f, 509.5f)
                quadTo(364f, 423f, 364f, 300f)
                quadTo(364f, 280f, 367f, 260f)
                quadTo(370f, 240f, 375f, 220f)
                quadTo(297f, 252f, 248.5f, 322f)
                quadTo(200f, 392f, 200f, 480f)
                quadTo(200f, 596f, 282f, 678f)
                quadTo(364f, 760f, 480f, 760f)
                close()
                moveTo(470f, 490f)
                quadTo(470f, 490f, 470f, 490f)
                quadTo(470f, 490f, 470f, 490f)
                quadTo(470f, 490f, 470f, 490f)
                quadTo(470f, 490f, 470f, 490f)
                quadTo(470f, 490f, 470f, 490f)
                quadTo(470f, 490f, 470f, 490f)
                quadTo(470f, 490f, 470f, 490f)
                quadTo(470f, 490f, 470f, 490f)
                quadTo(470f, 490f, 470f, 490f)
                quadTo(470f, 490f, 470f, 490f)
                quadTo(470f, 490f, 470f, 490f)
                quadTo(470f, 490f, 470f, 490f)
                close()
            }
        }.build()

        return _DarkMode!!
    }

@Suppress("ObjectPropertyName")
private var _DarkMode: ImageVector? = null
