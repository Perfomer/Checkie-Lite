package com.perfomer.checkielite.common.ui.util

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Stable
class FocusedFieldScroller<Key>(
    private val scrollState: ScrollState,
) {
    private val fieldBounds = mutableStateMapOf<Key, Rect>()

    var containerBounds by mutableStateOf<Rect?>(null)
        private set

    var focusedFieldKey by mutableStateOf<Key?>(null)
        private set

    var scrollRequestId by mutableIntStateOf(0)
        private set

    fun updateContainerBounds(bounds: Rect) {
        containerBounds = bounds
    }

    fun updateFieldBounds(key: Key, bounds: Rect) {
        val previousBounds = fieldBounds.put(key, bounds)
        val sizeChanged = previousBounds != null && (previousBounds.width != bounds.width || previousBounds.height != bounds.height)

        if (sizeChanged && focusedFieldKey == key) {
            scrollRequestId++
        }
    }

    fun updateFieldFocus(key: Key, isFocused: Boolean) {
        if (isFocused) {
            focusedFieldKey = key
            scrollRequestId++
        } else if (focusedFieldKey == key) {
            focusedFieldKey = null
        }
    }

    fun requestScroll() {
        if (focusedFieldKey != null) {
            scrollRequestId++
        }
    }

    private fun focusedFieldBounds(): Rect? {
        val key = focusedFieldKey ?: return null
        return fieldBounds[key]
    }

    suspend fun scrollFocusedFieldIntoView(
        bottomObstructionPx: Float,
        topFieldSpacingPx: Float,
        bottomFieldSpacingPx: Float,
    ) {
        val container = containerBounds ?: return
        val field = focusedFieldBounds() ?: return

        withFrameNanos { }

        val visibleTop = container.top + topFieldSpacingPx
        val visibleBottom = container.bottom - bottomObstructionPx - bottomFieldSpacingPx
        val scrollDelta = when {
            field.bottom > visibleBottom -> field.bottom - visibleBottom
            field.top < visibleTop -> field.top - visibleTop
            else -> 0f
        }

        if (scrollDelta == 0f) return

        val targetScroll = (scrollState.value + scrollDelta)
            .roundToInt()
            .coerceIn(0, scrollState.maxValue)

        if (targetScroll != scrollState.value) {
            scrollState.animateScrollTo(targetScroll)
        }
    }
}

@Composable
fun <Key> rememberFocusedFieldScroller(
    scrollState: ScrollState,
    bottomObstruction: Dp = 0.dp,
    topFieldSpacing: Dp = 16.dp,
    bottomFieldSpacing: Dp = 24.dp,
): FocusedFieldScroller<Key> {
    val density = LocalDensity.current
    val imeBottomPx = WindowInsets.ime.getBottom(density).toFloat()
    val bottomObstructionPx = with(density) { bottomObstruction.toPx() }
    val topFieldSpacingPx = with(density) { topFieldSpacing.toPx() }
    val bottomFieldSpacingPx = with(density) { bottomFieldSpacing.toPx() }
    val totalBottomObstructionPx = imeBottomPx + bottomObstructionPx
    val scroller = remember(scrollState) { FocusedFieldScroller<Key>(scrollState) }
    var lastTotalBottomObstructionPx by remember { mutableFloatStateOf(totalBottomObstructionPx) }

    LaunchedEffect(totalBottomObstructionPx) {
        if (lastTotalBottomObstructionPx != totalBottomObstructionPx) {
            lastTotalBottomObstructionPx = totalBottomObstructionPx
            scroller.requestScroll()
        }
    }

    LaunchedEffect(scroller.scrollRequestId) {
        scroller.scrollFocusedFieldIntoView(
            bottomObstructionPx = totalBottomObstructionPx,
            topFieldSpacingPx = topFieldSpacingPx,
            bottomFieldSpacingPx = bottomFieldSpacingPx,
        )
    }

    return scroller
}

fun <Key> Modifier.focusedFieldScrollContainer(
    scroller: FocusedFieldScroller<Key>,
): Modifier = onGloballyPositioned { coordinates ->
    scroller.updateContainerBounds(coordinates.boundsInRoot())
}

fun <Key> Modifier.focusedFieldScrollTarget(
    key: Key,
    scroller: FocusedFieldScroller<Key>,
): Modifier = onGloballyPositioned { coordinates ->
    scroller.updateFieldBounds(key, coordinates.boundsInRoot())
}.onFocusChanged { focusState ->
    scroller.updateFieldFocus(key, focusState.isFocused)
}
