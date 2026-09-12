package com.perfomer.checkielite.core.navigation.transition

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

/** Identities are local to two navigation entries, never global image-cache keys. */
@Stable
class SharedNavigationImageRegistry {
    private val participants = mutableStateMapOf<Any, Participant>()

    fun register(token: Any, key: String, isTarget: Boolean, isEnabled: () -> Boolean) {
        participants[token] = Participant(key, isTarget, isEnabled)
    }

    fun unregister(token: Any) {
        participants.remove(token)
    }

    /** An unmatched element must stay enabled so Compose can record its initial bounds. */
    fun isAmbiguous(key: String): Boolean = listOf(false, true).any { side ->
        participants.values.count { it.key == key && it.isTarget == side && it.isEnabled() } > 1
    }

    private class Participant(val key: String, val isTarget: Boolean, val isEnabled: () -> Boolean)
}

class SharedNavigationImageScope(
    val registry: SharedNavigationImageRegistry,
    val visibilityScope: AnimatedVisibilityScope,
    val isTarget: Boolean,
)

val LocalSharedNavigationImageScope = compositionLocalOf<SharedNavigationImageScope?> { null }

private data class ImageKey(val registry: SharedNavigationImageRegistry, val uri: String)

/** Shares a visible image only with an unambiguous counterpart in the active navigation pair. */
@Composable
fun Modifier.sharedNavigationImage(
    imageUri: String,
    isEnabled: () -> Boolean = { true },
): Modifier {
    val shared = LocalSharedTransitionScope.current ?: return this
    val scope = LocalSharedNavigationImageScope.current ?: return this
    val content = LocalSharedNavigationContent.current
    val enabled = rememberUpdatedState { isEnabled() && content?.isEnabled?.invoke() != false }
    val token = remember { Any() }
    DisposableEffect(scope.registry, imageUri, scope.isTarget) {
        scope.registry.register(token, imageUri, scope.isTarget) { enabled.value() }
        onDispose { scope.registry.unregister(token) }
    }
    val config = remember(scope.registry, imageUri) {
        object : SharedTransitionScope.SharedContentConfig {
            override val SharedTransitionScope.SharedContentState.isEnabled: Boolean
                get() = enabled.value() && !scope.registry.isAmbiguous(imageUri)
        }
    }
    return with(shared) {
        sharedBounds(
            sharedContentState = rememberSharedContentState(ImageKey(scope.registry, imageUri), config),
            animatedVisibilityScope = scope.visibilityScope,
            boundsTransform = { _, _ -> sharedNavigationTween() },
            enter = fadeIn(sharedNavigationTween()),
            exit = fadeOut(sharedNavigationTween()),
            resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(ContentScale.FillWidth),
            zIndexInOverlay = 3F,
        )
    }
}
