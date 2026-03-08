package ru.resodostudio.muzyakich.core.designsystem.icon.filled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.resodostudio.muzyakich.core.designsystem.icon.MuzIcons

val MuzIcons.Filled.Settings: ImageVector
    get() {
        if (_Settings != null) {
            return _Settings!!
        }
        _Settings = ImageVector.Builder(
            name = "Filled.Settings",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(433f, 880f)
                quadTo(406f, 880f, 386.5f, 862f)
                quadTo(367f, 844f, 363f, 818f)
                lineTo(354f, 752f)
                quadTo(341f, 747f, 329.5f, 740f)
                quadTo(318f, 733f, 307f, 725f)
                lineTo(245f, 751f)
                quadTo(220f, 762f, 195f, 753f)
                quadTo(170f, 744f, 156f, 721f)
                lineTo(109f, 639f)
                quadTo(95f, 616f, 101f, 590f)
                quadTo(107f, 564f, 128f, 547f)
                lineTo(181f, 507f)
                quadTo(180f, 500f, 180f, 493.5f)
                quadTo(180f, 487f, 180f, 480f)
                quadTo(180f, 473f, 180f, 466.5f)
                quadTo(180f, 460f, 181f, 453f)
                lineTo(128f, 413f)
                quadTo(107f, 396f, 101f, 370f)
                quadTo(95f, 344f, 109f, 321f)
                lineTo(156f, 239f)
                quadTo(170f, 216f, 195f, 207f)
                quadTo(220f, 198f, 245f, 209f)
                lineTo(307f, 235f)
                quadTo(318f, 227f, 330f, 220f)
                quadTo(342f, 213f, 354f, 208f)
                lineTo(363f, 142f)
                quadTo(367f, 116f, 386.5f, 98f)
                quadTo(406f, 80f, 433f, 80f)
                lineTo(527f, 80f)
                quadTo(554f, 80f, 573.5f, 98f)
                quadTo(593f, 116f, 597f, 142f)
                lineTo(606f, 208f)
                quadTo(619f, 213f, 630.5f, 220f)
                quadTo(642f, 227f, 653f, 235f)
                lineTo(715f, 209f)
                quadTo(740f, 198f, 765f, 207f)
                quadTo(790f, 216f, 804f, 239f)
                lineTo(851f, 321f)
                quadTo(865f, 344f, 859f, 370f)
                quadTo(853f, 396f, 832f, 413f)
                lineTo(779f, 453f)
                quadTo(780f, 460f, 780f, 466.5f)
                quadTo(780f, 473f, 780f, 480f)
                quadTo(780f, 487f, 780f, 493.5f)
                quadTo(780f, 500f, 778f, 507f)
                lineTo(831f, 547f)
                quadTo(852f, 564f, 858f, 590f)
                quadTo(864f, 616f, 850f, 639f)
                lineTo(802f, 721f)
                quadTo(788f, 744f, 763f, 753f)
                quadTo(738f, 762f, 713f, 751f)
                lineTo(653f, 725f)
                quadTo(642f, 733f, 630f, 740f)
                quadTo(618f, 747f, 606f, 752f)
                lineTo(597f, 818f)
                quadTo(593f, 844f, 573.5f, 862f)
                quadTo(554f, 880f, 527f, 880f)
                lineTo(433f, 880f)
                close()
                moveTo(482f, 620f)
                quadTo(540f, 620f, 581f, 579f)
                quadTo(622f, 538f, 622f, 480f)
                quadTo(622f, 422f, 581f, 381f)
                quadTo(540f, 340f, 482f, 340f)
                quadTo(423f, 340f, 382.5f, 381f)
                quadTo(342f, 422f, 342f, 480f)
                quadTo(342f, 538f, 382.5f, 579f)
                quadTo(423f, 620f, 482f, 620f)
                close()
            }
        }.build()

        return _Settings!!
    }

@Suppress("ObjectPropertyName")
private var _Settings: ImageVector? = null
