package com.perfomer.checkielite.core.navigation.transition

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.animateFloat
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

internal const val SharedNavigationImageZIndex = 3F
private const val NavigationForegroundZIndex = SharedNavigationImageZIndex + 1F

/** Keeps screen controls above shared images, including the screen's navigation fade. */
@Composable
fun Modifier.navigationForeground(): Modifier {
    val shared = LocalSharedTransitionScope.current ?: return this
    val scope = LocalSharedNavigationImageScope.current ?: return this
    val opacity = scope.visibilityScope.transition.animateFloat(
        transitionSpec = { sharedNavigationTween() },
        label = "Navigation foreground opacity",
    ) { if (it == EnterExitState.Visible) 1F else 0F }

    return with(shared) {
        renderInSharedTransitionScopeOverlay(zIndexInOverlay = NavigationForegroundZIndex)
            // Elevation bypasses ancestor alpha; apply the navigation fade inside the layer.
            .graphicsLayer { alpha = if (isTransitionActive) opacity.value else 1F }
    }
}
