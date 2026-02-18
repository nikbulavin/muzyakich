package ru.resodostudio.muzyakich.core.designsystem.theme

import androidx.compose.animation.BoundsTransform
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
val MotionScheme.sharedElementTransitionSpec: BoundsTransform
    @Composable get() = BoundsTransform { _, _ -> this@sharedElementTransitionSpec.defaultSpatialSpec() }
