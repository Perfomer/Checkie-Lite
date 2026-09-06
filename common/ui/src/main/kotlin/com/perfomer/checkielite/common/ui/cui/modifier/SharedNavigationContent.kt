package com.perfomer.checkielite.common.ui.cui.modifier

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.perfomer.checkielite.common.ui.presentation.transition.LocalNavigationAnimatedVisibilityScope
import com.perfomer.checkielite.common.ui.presentation.transition.LocalSharedTransitionScope
import com.perfomer.checkielite.common.ui.presentation.transition.SharedNavigationTransitionDurationMillis

enum class SharedContentPart {
    Title,
    Subtitle,
    Value,
    Icon,
}

@Immutable
data class SharedContentKey(
    val contentId: String,
    val part: SharedContentPart,
)

/**
 * Text keeps each endpoint's line layout and crossfades its typography inside moving bounds.
 * Identical artwork can instead render once using [isSameContent]. Apply before size/clip.
 */
@Composable
fun Modifier.sharedNavigationContent(
    key: SharedContentKey,
    isEnabled: () -> Boolean = { true },
    isSameContent: Boolean = false,
): Modifier {
    val sharedScope = LocalSharedTransitionScope.current ?: return this
    val visibilityScope = LocalNavigationAnimatedVisibilityScope.current ?: return this
    val enabled by rememberUpdatedState(isEnabled)
    val config = remember {
        object : SharedTransitionScope.SharedContentConfig {
            override val SharedTransitionScope.SharedContentState.isEnabled: Boolean
                get() = enabled()
        }
    }

    return with(sharedScope) {
        val state = rememberSharedContentState(key = key, config = config)
        if (isSameContent) {
            sharedElement(
                sharedContentState = state,
                animatedVisibilityScope = visibilityScope,
                boundsTransform = { _, _ ->
                    tween(SharedNavigationTransitionDurationMillis, easing = FastOutSlowInEasing)
                },
                zIndexInOverlay = 2F,
            )
        } else {
            sharedBounds(
                sharedContentState = state,
                animatedVisibilityScope = visibilityScope,
                enter = fadeIn(tween(SharedNavigationTransitionDurationMillis)),
                exit = fadeOut(tween(SharedNavigationTransitionDurationMillis)),
                boundsTransform = { _, _ ->
                    tween(SharedNavigationTransitionDurationMillis, easing = FastOutSlowInEasing)
                },
                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.TopStart,
                ),
                zIndexInOverlay = 2F,
            )
        }
    }
}
