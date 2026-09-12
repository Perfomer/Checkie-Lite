package com.perfomer.checkielite.core.navigation.transition

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp

/** Identities are local to two navigation entries, never global image-cache keys. */
@Stable
class SharedNavigationImageRegistry {
    private val participants = mutableStateMapOf<Any, Participant>()

    fun register(
        token: Any,
        key: String,
        isTarget: Boolean,
        cornerRadius: () -> Dp = { 0.dp },
        isEnabled: () -> Boolean,
    ) {
        participants[token] = Participant(key, isTarget, cornerRadius, isEnabled)
    }

    fun unregister(token: Any) {
        participants.remove(token)
    }

    /** An unmatched element must stay enabled so Compose can record its initial bounds. */
    fun isAmbiguous(key: String): Boolean = listOf(false, true).any { side ->
        participants.values.count { it.key == key && it.isTarget == side && it.isEnabled() } > 1
    }

    fun cornerRadius(key: String, isTarget: Boolean): Dp? = participants.values
        .singleOrNull { it.key == key && it.isTarget == isTarget && it.isEnabled() }
        ?.cornerRadius?.invoke()

    private class Participant(
        val key: String,
        val isTarget: Boolean,
        val cornerRadius: () -> Dp,
        val isEnabled: () -> Boolean,
    )
}

class SharedNavigationImageScope(
    val registry: SharedNavigationImageRegistry,
    val visibilityScope: AnimatedVisibilityScope,
    val isTarget: Boolean,
)

val LocalSharedNavigationImageScope = compositionLocalOf<SharedNavigationImageScope?> { null }

private data class ImageKey(val registry: SharedNavigationImageRegistry, val uri: String)

/**
 * Shares a visible image only with an unambiguous counterpart in the active navigation pair.
 * Both endpoints must center-crop the image to their measured bounds; letterboxing belongs outside.
 */
@Composable
fun Modifier.sharedNavigationImage(
    imageUri: String,
    cornerRadius: Dp = 0.dp,
    isEnabled: () -> Boolean = { true },
): Modifier {
    val restingShape = remember(cornerRadius) { ImageClipShape(cornerRadius) }
    val shared = LocalSharedTransitionScope.current ?: return clip(restingShape)
    val scope = LocalSharedNavigationImageScope.current ?: return clip(restingShape)
    val content = LocalSharedNavigationContent.current
    val enabled = rememberUpdatedState { isEnabled() && content?.isEnabled?.invoke() != false }
    val currentRadius = rememberUpdatedState(cornerRadius)
    val token = remember { Any() }
    DisposableEffect(scope.registry, imageUri, scope.isTarget) {
        scope.registry.register(token, imageUri, scope.isTarget, { currentRadius.value }) { enabled.value() }
        onDispose { scope.registry.unregister(token) }
    }
    val config = remember(scope.registry, imageUri) {
        object : SharedTransitionScope.SharedContentConfig {
            override val SharedTransitionScope.SharedContentState.isEnabled: Boolean
                get() = enabled.value() && !scope.registry.isAmbiguous(imageUri)
        }
    }
    val progress = scope.visibilityScope.transition.animateFloat(
        transitionSpec = { sharedNavigationTween() },
        label = "Shared navigation image corners",
    ) { state ->
        val side = if (state == EnterExitState.Visible) scope.isTarget else !scope.isTarget
        if (side) 1F else 0F
    }
    // Resolve endpoints during drawing, after both DisposableEffects have registered them.
    // Animating Dp directly can capture a missing counterpart's fallback on the first frame.
    val animatedShape = remember(scope, imageUri) {
        object : Shape {
            override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
                val source = scope.registry.cornerRadius(imageUri, isTarget = false) ?: currentRadius.value
                val target = scope.registry.cornerRadius(imageUri, isTarget = true) ?: currentRadius.value
                return ImageClipShape(lerp(source, target, progress.value)).createOutline(size, layoutDirection, density)
            }
        }
    }
    return with(shared) {
        val contentState = rememberSharedContentState(ImageKey(scope.registry, imageUri), config)
        // Keep the outgoing renderer until the bounds arrive; sharedElement switches to the
        // incoming thumbnail at the start, which can already contain its final crop.
        sharedBounds(
            sharedContentState = contentState,
            animatedVisibilityScope = scope.visibilityScope,
            boundsTransform = { _, _ -> sharedNavigationTween() },
            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
            enter = EnterTransition.None,
            exit = ExitTransition.None,
            zIndexInOverlay = SharedNavigationImageZIndex,
            clipInOverlayDuringTransition = OverlayClip(animatedShape),
        )
            .drawWithCache {
                val shape = if (contentState.isMatchFound) animatedShape else restingShape
                val path = Path().apply {
                    addOutline(shape.createOutline(size, layoutDirection, this@drawWithCache))
                }
                onDrawWithContent {
                    // Remeasurement keeps local and overlay geometry identical. Keep clipping
                    // throughout the handoff rather than exposing an unclipped child for a frame.
                    if (shouldDrawSharedImage(contentState.isMatchFound, scope.visibilityScope.transition.currentState)) {
                        clipPath(path) { this@onDrawWithContent.drawContent() }
                    }
                }
            }
    }
}

/** Visibility changes only after the transition finishes, including a cancelled/reversed seek. */
internal fun shouldDrawSharedImage(isMatchFound: Boolean, currentState: EnterExitState): Boolean =
    !isMatchFound || currentState == EnterExitState.Visible

/** The image and its clipping share the same animated container, with radius in screen pixels. */
internal data class ImageClipShape(val radius: Dp) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val bounds = Rect(0F, 0F, size.width, size.height)
        val corner = with(density) { radius.toPx() }.coerceAtMost(minOf(size.width, size.height) / 2F)
        return Outline.Rounded(RoundRect(bounds, CornerRadius(corner)))
    }
}
