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
    val role: SharedContentRole = SharedContentRole.Default(group),
    val isSource: Boolean = true,
    val isEnabled: () -> Boolean,
) {
    init {
        require(role.group == group) { "Content role must belong to its content group" }
    }

    val key = SharedNavigationContentKey(pair, group, id, pair?.match(role, isSource))

    fun isTransitionEnabled(): Boolean = isEnabled() && key.match != null
}

val LocalSharedNavigationContent = compositionLocalOf<SharedNavigationContentState?> { null }

/** Supplies one content identity to every shared element in [content]. */
@Composable
fun SharedNavigationContent(
    id: Any?,
    group: SharedContentGroup? = null,
    role: SharedContentRole? = null,
    isEnabled: () -> Boolean = { true },
    content: @Composable () -> Unit,
) {
    if (id == null) {
        content()
    } else {
        val parent = LocalSharedNavigationContent.current
        val resolvedGroup = group ?: role?.group ?: parent?.group ?: SharedContentGroup.Default
        val resolvedRole = role ?: parent?.role?.takeIf { it.group == resolvedGroup }
            ?: SharedContentRole.Default(resolvedGroup)
        val endpoint = LocalSharedNavigationEndpoint.current
        val enabled by rememberUpdatedState(isEnabled)
        val state = remember(id, resolvedGroup, resolvedRole, endpoint) {
            SharedNavigationContentState(
                id = id,
                group = resolvedGroup,
                pair = endpoint?.pair,
                role = resolvedRole,
                isSource = endpoint?.isSource == true,
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
