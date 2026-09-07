package com.perfomer.checkielite.core.navigation.transition

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

const val SharedNavigationTransitionDurationMillis: Int = 300

val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

val LocalNavigationAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

@Stable
class SharedNavigationContentState internal constructor(
    val id: Any,
    val isEnabled: () -> Boolean,
)

val LocalSharedNavigationContent = compositionLocalOf<SharedNavigationContentState?> { null }

/** Supplies one content identity to every shared element in [content]. */
@Composable
fun SharedNavigationContent(
    id: Any?,
    isEnabled: () -> Boolean = { true },
    content: @Composable () -> Unit,
) {
    if (id == null) {
        content()
    } else {
        val enabled by rememberUpdatedState(isEnabled)
        val state = remember(id) {
            SharedNavigationContentState(
                id = id,
                isEnabled = { enabled() },
            )
        }
        CompositionLocalProvider(
            LocalSharedNavigationContent provides state,
            content = content,
        )
    }
}

@Composable
fun rememberSharedContentConfig(
    content: SharedNavigationContentState,
): SharedTransitionScope.SharedContentConfig {
    val enabled by rememberUpdatedState(content.isEnabled)
    return remember {
        object : SharedTransitionScope.SharedContentConfig {
            override val SharedTransitionScope.SharedContentState.isEnabled: Boolean
                get() = enabled()
        }
    }
}
