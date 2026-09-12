package com.perfomer.checkielite.core.navigation.transition

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.Stable

const val SharedNavigationTransitionDurationMillis: Int = 300

val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

val LocalNavigationAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

@Stable
class SharedNavigationContentState internal constructor(
    val id: Any,
    val group: SharedContentGroup,
    val pair: SharedNavigationPair?,
    val isEnabled: () -> Boolean,
) {
    val key = SharedNavigationContentKey(pair, group, id)

    fun isTransitionEnabled(): Boolean = isEnabled() && pair?.allows(group) == true
}

val LocalSharedNavigationContent = compositionLocalOf<SharedNavigationContentState?> { null }

/** Supplies one content identity to every shared element in [content]. */
@Composable
fun SharedNavigationContent(
    id: Any?,
    group: SharedContentGroup? = null,
    isEnabled: () -> Boolean = { true },
    content: @Composable () -> Unit,
) {
    if (id == null) {
        content()
    } else {
        val resolvedGroup = group ?: LocalSharedNavigationContent.current?.group ?: SharedContentGroup.Default
        val pair = LocalSharedNavigationPair.current
        val enabled by rememberUpdatedState(isEnabled)
        val state = remember(id, resolvedGroup, pair) {
            SharedNavigationContentState(
                id = id,
                group = resolvedGroup,
                pair = pair,
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
    val enabled by rememberUpdatedState {
        content.isTransitionEnabled()
    }
    return remember {
        object : SharedTransitionScope.SharedContentConfig {
            override val SharedTransitionScope.SharedContentState.isEnabled: Boolean
                get() = enabled()
        }
    }
}
