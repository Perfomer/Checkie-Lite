package com.perfomer.checkielite.core.navigation.transition

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

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

/** Shares a visible image only with an unambiguous counterpart in the active navigation pair. */
@Composable
fun Modifier.sharedNavigationImage(
    imageUri: String,
    cornerRadius: Dp = 0.dp,
    isEnabled: () -> Boolean = { true },
): Modifier {
    val restingShape = remember(cornerRadius) { ImageClipShape(cornerRadius, IntSize.Zero) }
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
    val radius by scope.visibilityScope.transition.animateDp(
        transitionSpec = { sharedNavigationTween() },
        label = "Shared navigation image corners",
    ) { state ->
        val side = if (state == EnterExitState.Visible) scope.isTarget else !scope.isTarget
        scope.registry.cornerRadius(imageUri, side) ?: cornerRadius
    }
    var contentSize by remember { mutableStateOf(IntSize.Zero) }
    val overlayShape = remember(radius, contentSize) { ImageClipShape(radius, contentSize) }
    return with(shared) {
        val contentState = rememberSharedContentState(ImageKey(scope.registry, imageUri), config)
        sharedBounds(
            sharedContentState = contentState,
            animatedVisibilityScope = scope.visibilityScope,
            boundsTransform = { _, _ -> sharedNavigationTween() },
            enter = fadeIn(sharedNavigationTween()),
            exit = fadeOut(sharedNavigationTween()),
            resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(ContentScale.FillWidth),
            zIndexInOverlay = 3F,
            clipInOverlayDuringTransition = OverlayClip(overlayShape),
        ).onSizeChanged { contentSize = it }
            // Clip in transition coordinates, without scaling or truncating the animated radius.
            .clip(if (contentState.isMatchFound && isTransitionActive) RectangleShape else restingShape)
    }
}

/** FillWidth centers each endpoint's content inside the shared bounds; clip its visible extent. */
internal data class ImageClipShape(val radius: Dp, val contentSize: IntSize) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val height = if (contentSize.width == 0) size.height else {
            (size.width * contentSize.height / contentSize.width).coerceAtMost(size.height)
        }
        val top = (size.height - height) / 2F
        val bounds = Rect(0F, top, size.width, top + height)
        val corner = with(density) { radius.toPx() }.coerceAtMost(minOf(size.width, height) / 2F)
        return Outline.Rounded(RoundRect(bounds, CornerRadius(corner)))
    }
}
