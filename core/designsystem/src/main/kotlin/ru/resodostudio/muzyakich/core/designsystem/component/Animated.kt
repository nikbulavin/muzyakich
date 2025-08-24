package ru.resodostudio.muzyakich.core.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedIcon(
    targetState: Boolean,
    baseIcon: ImageVector,
    targetIcon: ImageVector,
    modifier: Modifier = Modifier,
    label: String = "AnimatedIcon",
    baseContentDescription: String? = null,
    targetContentDescription: String? = null,
) {
    val effectsSpec = MaterialTheme.motionScheme.slowEffectsSpec<Float>()
    val spatialSpec = MaterialTheme.motionScheme.slowSpatialSpec<Float>()
    AnimatedContent(
        targetState = targetState,
        label = label,
        transitionSpec = {
            fadeIn(effectsSpec) + scaleIn(spatialSpec) togetherWith
                    fadeOut(effectsSpec) + scaleOut(spatialSpec)
        },
        modifier = modifier,
    ) { state ->
        if (state) {
            Icon(
                imageVector = baseIcon,
                contentDescription = baseContentDescription,
            )
        } else {
            Icon(
                imageVector = targetIcon,
                contentDescription = targetContentDescription,
            )
        }
    }
}