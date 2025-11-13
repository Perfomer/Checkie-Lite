package com.perfomer.checkielite.common.ui.cui.modifier

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.PlaceHolderSize
import androidx.compose.animation.SharedTransitionScope.PlaceHolderSize.Companion.contentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Rect

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope> { error("Not provided") }
val LocalAnimatedVisibilityScope = staticCompositionLocalOf<AnimatedVisibilityScope> { error("Not provided") }

@OptIn(ExperimentalSharedTransitionApi::class)
private val defaultBoundsTransform = BoundsTransform { _, _ ->
    spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = Rect.VisibilityThreshold,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun Modifier.localSharedElement(
    key: Any,
    boundsTransform: BoundsTransform = defaultBoundsTransform,
    placeHolderSize: PlaceHolderSize = contentSize,
    renderInOverlayDuringTransition: Boolean = true,
    zIndexInOverlay: Float = 0f,
) = composed {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current

    with(sharedTransitionScope) {
        this@localSharedElement.sharedElement(
            sharedContentState = rememberSharedContentState(key = key),
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = boundsTransform,
            placeHolderSize = placeHolderSize,
            renderInOverlayDuringTransition = renderInOverlayDuringTransition,
            zIndexInOverlay = zIndexInOverlay,
        )
    }
}