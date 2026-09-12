package com.perfomer.checkielite.core.navigation.transition

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier

interface SharedNavigationElement

@Immutable
private data class SharedNavigationElementKey(
    val contentId: Any,
    val element: SharedNavigationElement,
)

/**
 * Shares a named element from the nearest [SharedNavigationContent].
 * Text keeps each endpoint's layout and crossfades inside the moving bounds.
 */
@Composable
fun Modifier.sharedNavigationElement(
    element: SharedNavigationElement,
    isSameContent: Boolean = false,
    resizeMode: SharedTransitionScope.ResizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(
        contentScale = ContentScale.Fit,
        alignment = Alignment.TopStart,
    ),
): Modifier {
    val sharedScope = LocalSharedTransitionScope.current ?: return this
    val visibilityScope = LocalNavigationAnimatedVisibilityScope.current ?: return this
    val content = LocalSharedNavigationContent.current ?: return this
    val config = rememberSharedContentConfig(content)

    return with(sharedScope) {
        val state = rememberSharedContentState(
            key = SharedNavigationElementKey(content.key, element),
            config = config,
        )
        if (isSameContent) {
            sharedElement(
                sharedContentState = state,
                animatedVisibilityScope = visibilityScope,
                boundsTransform = { _, _ -> sharedNavigationTween() },
                zIndexInOverlay = 2F,
            )
        } else {
            sharedBounds(
                sharedContentState = state,
                animatedVisibilityScope = visibilityScope,
                enter = fadeIn(sharedNavigationTween()),
                exit = fadeOut(sharedNavigationTween()),
                boundsTransform = { _, _ -> sharedNavigationTween() },
                resizeMode = resizeMode,
                zIndexInOverlay = 2F,
            )
        }
    }
}

fun <T> sharedNavigationTween() = tween<T>(
    durationMillis = SharedNavigationTransitionDurationMillis,
    easing = FastOutSlowInEasing,
)
