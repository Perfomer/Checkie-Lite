package com.perfomer.checkielite.common.ui.presentation.transition

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.perfomer.checkielite.core.navigation.transition.LocalNavigationAnimatedVisibilityScope
import com.perfomer.checkielite.core.navigation.transition.LocalSharedNavigationContent
import com.perfomer.checkielite.core.navigation.transition.LocalSharedTransitionScope
import com.perfomer.checkielite.core.navigation.transition.rememberSharedContentConfig
import com.perfomer.checkielite.core.navigation.transition.sharedNavigationTween

@Immutable
private data class SharedContainerKey(val contentId: Any, val isSurface: Boolean)

/**
 * Expands the surface while keeping both endpoint layouts at their original size and position.
 * Reports an active transition to content only when this container has a shared counterpart.
 */
@Composable
fun SharedNavigationContainer(
    cornerRadius: Dp,
    overlayCornerRadius: Dp,
    color: Color,
    overlayColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable (isTransitionActive: Boolean) -> Unit,
) {
    val sharedScope = LocalSharedTransitionScope.current
    val visibilityScope = LocalNavigationAnimatedVisibilityScope.current
    val sharedContent = LocalSharedNavigationContent.current
    if (sharedContent == null || sharedScope == null || visibilityScope == null) {
        val shape = RoundedCornerShape(cornerRadius)
        Box(modifier = modifier.background(color, shape).clip(shape)) {
            content(false)
        }
        return
    }

    val config = rememberSharedContentConfig(sharedContent)
    val radius by visibilityScope.transition.animateDp(
        transitionSpec = { sharedNavigationTween() },
        label = "Container corners",
    ) { if (it == EnterExitState.Visible) cornerRadius else overlayCornerRadius }
    val backgroundColor by visibilityScope.transition.animateColor(
        transitionSpec = { sharedNavigationTween() },
        label = "Container color",
    ) { if (it == EnterExitState.Visible) color else overlayColor }

    with(sharedScope) {
        val surfaceState = rememberSharedContentState(SharedContainerKey(sharedContent.id, isSurface = true), config)
        val contentState = rememberSharedContentState(SharedContainerKey(sharedContent.id, isSurface = false), config)
        val shape = RoundedCornerShape(if (surfaceState.isMatchFound) radius else cornerRadius)
        Box(modifier = modifier) {
            // One opaque surface sits below both endpoint contents, so neither can cover the other.
            Spacer(
                modifier = Modifier
                    .matchParentSize()
                    .sharedElement(
                        sharedContentState = surfaceState,
                        animatedVisibilityScope = visibilityScope,
                        boundsTransform = { _, _ -> sharedNavigationTween() },
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
                        enter = fadeIn(sharedNavigationTween()),
                        exit = fadeOut(sharedNavigationTween()),
                        boundsTransform = { _, _ -> sharedNavigationTween() },
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                        clipInOverlayDuringTransition = OverlayClip(shape),
                        zIndexInOverlay = 0.1F,
                    )
                    .clip(shape)
                    // Keep LazyColumn/Scaffold layout and non-shared content anchored at each endpoint.
                    .skipToLookaheadSize { contentState.isMatchFound && isTransitionActive }
                    .skipToLookaheadPosition { contentState.isMatchFound && isTransitionActive }
            ) {
                content(contentState.isMatchFound && isTransitionActive)
            }
        }
    }
}
