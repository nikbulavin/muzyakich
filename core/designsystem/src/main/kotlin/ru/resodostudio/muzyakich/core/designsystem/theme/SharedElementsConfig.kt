package ru.resodostudio.muzyakich.core.designsystem.theme

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloat
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toPath
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.rectangle
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import kotlin.math.max

sealed interface SharedElementKey {
    object NowPlayingBarToPlayerScreen : SharedElementKey
}

val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope> {
    throw IllegalStateException("No SharedTransitionScope provided")
}

class MorphOverlayClip(val morph: Morph, private val animatedProgress: () -> Float) :
    SharedTransitionScope.OverlayClip {
    private val matrix = Matrix()

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    override fun getClipPath(
        sharedContentState: SharedTransitionScope.SharedContentState,
        bounds: Rect,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Path {
        matrix.reset()
        val max = max(bounds.width, bounds.height)
        matrix.scale(max, max)

        val path = morph.toPath(progress = animatedProgress.invoke())
        path.transform(matrix)
        path.translate(bounds.center + Offset(-max / 2f, -max / 2f))
        return path
    }
}

@Composable
fun Modifier.sharedBoundsRevealWithShapeMorph(
    sharedContentState: SharedTransitionScope.SharedContentState,
    sharedTransitionScope: SharedTransitionScope = LocalSharedTransitionScope.current,
    animatedVisibilityScope: AnimatedVisibilityScope = LocalNavAnimatedContentScope.current,
    boundsTransform: BoundsTransform = MaterialTheme.motionScheme.sharedElementTransitionSpec,
    resizeMode: SharedTransitionScope.ResizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
    restingShape: RoundedPolygon = RoundedPolygon.rectangle().normalized(),
    targetShape: RoundedPolygon = RoundedPolygon.circle().normalized(),
    renderInOverlayDuringTransition: Boolean = true,
    targetValueByState: @Composable (state: EnterExitState) -> Float = {
        when (it) {
            EnterExitState.PreEnter -> 1f
            EnterExitState.Visible -> 0f
            EnterExitState.PostExit -> 1f
        }
    },
    keepChildrenSizePlacement: Boolean = true,
): Modifier {
    with(sharedTransitionScope) {
        val animatedProgress =
            animatedVisibilityScope.transition.animateFloat(targetValueByState = targetValueByState)

        val morph = remember {
            Morph(restingShape, targetShape)
        }
        val morphClip = MorphOverlayClip(morph) { animatedProgress.value }
        val modifier = if (keepChildrenSizePlacement) {
            Modifier
                .skipToLookaheadSize()
                .skipToLookaheadPosition()
        } else {
            Modifier
        }
        return this@sharedBoundsRevealWithShapeMorph
            .sharedBounds(
                sharedContentState = sharedContentState,
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = boundsTransform,
                resizeMode = resizeMode,
                clipInOverlayDuringTransition = morphClip,
                renderInOverlayDuringTransition = renderInOverlayDuringTransition,
            )
            .then(modifier)
    }
}