package com.perfomer.checkielite.common.ui.presentation.transition

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Immutable
private data class SharedContainerKey(val contentId: String, val isSurface: Boolean)

/** Expands the surface while keeping both endpoint layouts at their original size and position. */
@Composable
fun SharedNavigationContainer(
    contentId: String?,
    cornerRadius: Dp,
    otherCornerRadius: Dp,
    color: Color,
    otherColor: Color,
    isEnabled: () -> Boolean = { true },
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val sharedScope = LocalSharedTransitionScope.current
    val visibilityScope = LocalNavigationAnimatedVisibilityScope.current
    if (contentId == null || sharedScope == null || visibilityScope == null) {
        val shape = RoundedCornerShape(cornerRadius)
        Box(modifier = modifier.background(color, shape).clip(shape)) {
            content()
        }
        return
    }

    val enabled by rememberUpdatedState(isEnabled)
    val config = remember {
        object : SharedTransitionScope.SharedContentConfig {
            override val SharedTransitionScope.SharedContentState.isEnabled: Boolean
                get() = enabled()
        }
    }
    val radius by visibilityScope.transition.animateDp(
        transitionSpec = { tween(SharedNavigationTransitionDurationMillis, easing = FastOutSlowInEasing) },
        label = "Container corners",
    ) { if (it == EnterExitState.Visible) cornerRadius else otherCornerRadius }
    val backgroundColor by visibilityScope.transition.animateColor(
        transitionSpec = { tween(SharedNavigationTransitionDurationMillis, easing = FastOutSlowInEasing) },
        label = "Container color",
    ) { if (it == EnterExitState.Visible) color else otherColor }

    with(sharedScope) {
        val surfaceState = rememberSharedContentState(SharedContainerKey(contentId, isSurface = true), config)
        val contentState = rememberSharedContentState(SharedContainerKey(contentId, isSurface = false), config)
        val shape = RoundedCornerShape(if (surfaceState.isMatchFound) radius else cornerRadius)
        Box(modifier = modifier) {
            // One opaque surface sits below both endpoint contents, so neither can cover the other.
            Spacer(
                modifier = Modifier
                    .matchParentSize()
                    .sharedElement(
                        sharedContentState = surfaceState,
                        animatedVisibilityScope = visibilityScope,
                        boundsTransform = { _, _ ->
                            tween(SharedNavigationTransitionDurationMillis, easing = FastOutSlowInEasing)
                        },
                        clipInOverlayDuringTransition = OverlayClip(shape),
                        zIndexInOverlay = 0F,
                    )
                    .background(if (surfaceState.isMatchFound) backgroundColor else color, shape)
            )
            Box(
                modifier = Modifier
                    .sharedBounds(
                        sharedContentState = contentState,
                        animatedVisibilityScope = visibilityScope,
                        enter = fadeIn(tween(SharedNavigationTransitionDurationMillis)),
                        exit = fadeOut(tween(SharedNavigationTransitionDurationMillis)),
                        boundsTransform = { _, _ ->
                            tween(SharedNavigationTransitionDurationMillis, easing = FastOutSlowInEasing)
                        },
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                        clipInOverlayDuringTransition = OverlayClip(shape),
                        zIndexInOverlay = 0.1F,
                    )
                    .clip(shape)
                    // Keep LazyColumn/Scaffold layout and non-shared content anchored at each endpoint.
                    .skipToLookaheadSize { contentState.isMatchFound && isTransitionActive }
                    .skipToLookaheadPosition { contentState.isMatchFound && isTransitionActive }
            ) {
                content()
            }
        }
    }
}
