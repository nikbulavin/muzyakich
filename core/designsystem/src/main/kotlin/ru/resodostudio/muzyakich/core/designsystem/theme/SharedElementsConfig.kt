package ru.resodostudio.muzyakich.core.designsystem.theme

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf

data class SharedElementKey(
    val id: String,
    val origin: String,
    val type: SharedElementType,
)

enum class SharedElementType {
    Bounds,
    Artwork,
    Title,
    Artist,
}

val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope> {
    throw IllegalStateException("No SharedTransitionScope provided")
}