package com.perfomer.checkielite.common.ui.cui.widget.reorder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val CuiReorderableLazyRowAutoScrollThreshold = 72.dp
private val CuiReorderableLazyRowAutoScrollMaxSpeed = 360.dp
private val CuiReorderableLazyRowAutoScrollActivationDistance = 18.dp

@Composable
fun <K : Any> rememberCuiReorderableLazyRowState(
    itemSpacing: Dp,
    autoScrollThreshold: Dp = CuiReorderableLazyRowAutoScrollThreshold,
    autoScrollMaxSpeed: Dp = CuiReorderableLazyRowAutoScrollMaxSpeed,
    autoScrollActivationDistance: Dp = CuiReorderableLazyRowAutoScrollActivationDistance,
): CuiReorderableLazyRowState<K> {
    val density = LocalDensity.current
    val autoScrollThresholdPx = with(density) { autoScrollThreshold.toPx() }
    val autoScrollMaxSpeedPxPerSecond = with(density) { autoScrollMaxSpeed.toPx() }
    val autoScrollActivationDistancePx = with(density) { autoScrollActivationDistance.toPx() }
    val itemSpacingPx = with(density) { itemSpacing.toPx() }

    return remember(
        autoScrollThresholdPx,
        autoScrollMaxSpeedPxPerSecond,
        autoScrollActivationDistancePx,
        itemSpacingPx,
    ) {
        CuiReorderableLazyRowState(
            autoScrollThresholdPx = autoScrollThresholdPx,
            autoScrollMaxSpeedPxPerSecond = autoScrollMaxSpeedPxPerSecond,
            autoScrollActivationDistancePx = autoScrollActivationDistancePx,
            itemSpacingPx = itemSpacingPx,
        )
    }
}

@Stable
class CuiReorderableLazyRowState<K : Any> internal constructor(
    private val autoScrollThresholdPx: Float,
    private val autoScrollMaxSpeedPxPerSecond: Float,
    private val autoScrollActivationDistancePx: Float,
    private val itemSpacingPx: Float,
) {
    private val itemBounds = mutableStateMapOf<K, CuiReorderableLazyRowItemLayout>()

    private var contentStartPx by mutableFloatStateOf(0f)
    private var contentEndPx by mutableFloatStateOf(0f)

    var draggedItemKey by mutableStateOf<K?>(null)
        private set

    var settlingItemKey by mutableStateOf<K?>(null)
        private set

    private var initialDraggedItemIndex by mutableIntStateOf(-1)

    private var draggedItemIndex by mutableIntStateOf(-1)

    var overlayLeftPx by mutableFloatStateOf(0f)
        private set

    private var dragDisplacementX by mutableFloatStateOf(0f)

    var settlingFromLeftPx by mutableFloatStateOf(0f)
        private set

    var settlingToLeftPx by mutableFloatStateOf(0f)
        private set

    var autoScrollVelocityPxPerSecond by mutableFloatStateOf(0f)
        private set

    val floatingItemKey: K?
        get() = draggedItemKey ?: settlingItemKey

    val isDragging: Boolean
        get() = draggedItemKey != null

    val isSettling: Boolean
        get() = settlingItemKey != null

    fun updateViewport(
        contentStartPx: Float,
        contentEndPx: Float,
    ) {
        this.contentStartPx = contentStartPx
        this.contentEndPx = contentEndPx
    }

    fun retainMeasuredItems(
        itemKeys: Set<K>,
    ) {
        itemBounds.keys.retainAll(itemKeys)
    }

    fun onItemMeasured(
        itemKey: K,
        leftPx: Float,
        topPx: Float,
        widthPx: Float,
        heightPx: Float,
    ) {
        itemBounds[itemKey] = CuiReorderableLazyRowItemLayout(
            leftPx = leftPx,
            topPx = topPx,
            widthPx = widthPx,
            heightPx = heightPx,
        )
    }

    fun getItemLayout(
        itemKey: K,
    ): CuiReorderableLazyRowItemLayout? {
        return itemBounds[itemKey]
    }

    fun findItemIndexAtPosition(
        itemKeys: List<K>,
        touchX: Float,
        touchY: Float,
    ): Int? {
        val itemIndex = itemKeys.indexOfFirst { itemKey ->
            itemBounds[itemKey]?.contains(
                xPx = touchX,
                yPx = touchY,
            ) == true
        }

        return itemIndex.takeIf { it >= 0 }
    }

    fun startDrag(
        itemKey: K,
        itemIndex: Int,
    ): Boolean {
        if (isDragging) return false

        val bounds = itemBounds[itemKey] ?: return false

        settlingItemKey = null
        settlingFromLeftPx = 0f
        settlingToLeftPx = 0f
        draggedItemKey = itemKey
        initialDraggedItemIndex = itemIndex
        draggedItemIndex = itemIndex
        overlayLeftPx = bounds.leftPx
        dragDisplacementX = 0f
        recalculateAutoScroll()
        return true
    }

    fun onDrag(
        itemKeys: List<K>,
        dragDeltaX: Float,
    ): CuiReorderableLazyRowMove? {
        if (!isDragging) return null

        overlayLeftPx += dragDeltaX
        dragDisplacementX += dragDeltaX
        return moveIfNeeded(itemKeys)
    }

    fun onScroll(
        itemKeys: List<K>,
    ): CuiReorderableLazyRowMove? {
        if (!isDragging) return null

        return moveIfNeeded(itemKeys)
    }

    fun finishDrag(
        itemKeys: List<K>,
    ): CuiReorderableLazyRowDropResult<K>? {
        return startSettling(itemKeys)
    }

    fun cancelDrag(
        itemKeys: List<K>,
    ): CuiReorderableLazyRowDropResult<K>? {
        return startSettling(itemKeys)
    }

    fun resetImmediately() {
        reset()
    }

    fun onSettlingFinished() {
        settlingItemKey = null
        settlingFromLeftPx = 0f
        settlingToLeftPx = 0f
        overlayLeftPx = 0f
    }

    fun stopAutoScroll() {
        autoScrollVelocityPxPerSecond = 0f
    }

    private fun moveIfNeeded(
        itemKeys: List<K>,
    ): CuiReorderableLazyRowMove? {
        val draggedKey = draggedItemKey ?: return null
        val currentIndex = resolveDraggedIndex(
            itemKeys = itemKeys,
            draggedKey = draggedKey,
        ) ?: return null
        val currentBounds = itemBounds[draggedKey] ?: return null.also {
            recalculateAutoScroll()
        }

        val draggedCenter = overlayLeftPx + currentBounds.widthPx / 2f
        val targetIndex = when {
            draggedCenter > currentBounds.centerPx -> {
                val nextIndex = currentIndex + 1
                val nextItemKey = itemKeys.getOrNull(nextIndex)
                val nextBounds = nextItemKey?.let(itemBounds::get)

                if (nextBounds != null && draggedCenter > nextBounds.centerPx) {
                    nextIndex
                } else {
                    null
                }
            }

            draggedCenter < currentBounds.centerPx -> {
                val previousIndex = currentIndex - 1
                val previousItemKey = itemKeys.getOrNull(previousIndex)
                val previousBounds = previousItemKey?.let(itemBounds::get)

                if (previousBounds != null && draggedCenter < previousBounds.centerPx) {
                    previousIndex
                } else {
                    null
                }
            }

            else -> null
        } ?: return null.also {
            recalculateAutoScroll()
        }

        draggedItemIndex = targetIndex
        recalculateAutoScroll()

        return CuiReorderableLazyRowMove(
            fromIndex = currentIndex,
            toIndex = targetIndex,
        )
    }

    private fun resolveDraggedIndex(
        itemKeys: List<K>,
        draggedKey: K,
    ): Int? {
        val currentIndex = draggedItemIndex
        if (currentIndex in itemKeys.indices && itemKeys[currentIndex] == draggedKey) {
            return currentIndex
        }

        val resolvedIndex = itemKeys.indexOfFirst { itemKey -> itemKey == draggedKey }
        if (resolvedIndex == -1) {
            reset()
            return null
        }

        draggedItemIndex = resolvedIndex
        return resolvedIndex
    }

    private fun recalculateAutoScroll() {
        val draggedKey = draggedItemKey ?: run {
            autoScrollVelocityPxPerSecond = 0f
            return
        }

        val draggedItemBounds = itemBounds[draggedKey] ?: run {
            autoScrollVelocityPxPerSecond = 0f
            return
        }

        val draggedItemStart = overlayLeftPx
        val draggedItemEnd = draggedItemStart + draggedItemBounds.widthPx
        val leftBoundary = contentStartPx + autoScrollThresholdPx
        val rightBoundary = contentEndPx - autoScrollThresholdPx

        autoScrollVelocityPxPerSecond = when {
            dragDisplacementX < -autoScrollActivationDistancePx &&
                draggedItemStart < leftBoundary -> {
                -calculateAutoScrollVelocity(leftBoundary - draggedItemStart)
            }

            dragDisplacementX > autoScrollActivationDistancePx &&
                draggedItemEnd > rightBoundary -> {
                calculateAutoScrollVelocity(draggedItemEnd - rightBoundary)
            }

            else -> 0f
        }
    }

    private fun calculateAutoScrollVelocity(
        overshoot: Float,
    ): Float {
        val progress = (overshoot / autoScrollThresholdPx).coerceIn(0f, 1f)
        return autoScrollMaxSpeedPxPerSecond * progress * progress
    }

    private fun reset() {
        settlingItemKey = null
        settlingFromLeftPx = 0f
        settlingToLeftPx = 0f
        clearDragState()
    }

    private fun startSettling(
        itemKeys: List<K>,
    ): CuiReorderableLazyRowDropResult<K>? {
        val draggedKey = draggedItemKey
        val dropResult = if (
            draggedKey != null &&
            draggedItemIndex != -1 &&
            draggedItemIndex != initialDraggedItemIndex
        ) {
            CuiReorderableLazyRowDropResult(
                itemKey = draggedKey,
                toPosition = draggedItemIndex,
            )
        } else {
            null
        }

        if (draggedKey != null) {
            val targetOffset = resolveSettlingTargetOffset(
                itemKeys = itemKeys,
                draggedKey = draggedKey,
                targetIndex = draggedItemIndex,
            )
            settlingItemKey = draggedKey
            settlingFromLeftPx = overlayLeftPx
            settlingToLeftPx = targetOffset
        }

        clearDragState()
        return dropResult
    }

    private fun clearDragState() {
        draggedItemKey = null
        initialDraggedItemIndex = -1
        draggedItemIndex = -1
        overlayLeftPx = 0f
        dragDisplacementX = 0f
        autoScrollVelocityPxPerSecond = 0f
    }

    private fun resolveSettlingTargetOffset(
        itemKeys: List<K>,
        draggedKey: K,
        targetIndex: Int,
    ): Float {
        val draggedBounds = itemBounds[draggedKey] ?: return overlayLeftPx
        val previousBounds = itemKeys
            .getOrNull(targetIndex - 1)
            ?.let(itemBounds::get)
        val nextBounds = itemKeys
            .getOrNull(targetIndex + 1)
            ?.let(itemBounds::get)

        return when {
            previousBounds != null && nextBounds != null -> {
                (previousBounds.leftPx + nextBounds.leftPx) / 2f
            }

            previousBounds != null -> {
                previousBounds.leftPx + previousBounds.widthPx + itemSpacingPx
            }

            nextBounds != null -> {
                nextBounds.leftPx - draggedBounds.widthPx - itemSpacingPx
            }

            else -> draggedBounds.leftPx
        }
    }
}

data class CuiReorderableLazyRowDropResult<K : Any>(
    val itemKey: K,
    val toPosition: Int,
)

data class CuiReorderableLazyRowMove(
    val fromIndex: Int,
    val toIndex: Int,
)

data class CuiReorderableLazyRowItemLayout(
    val leftPx: Float,
    val topPx: Float,
    val widthPx: Float,
    val heightPx: Float,
) {
    val centerPx: Float
        get() = leftPx + widthPx / 2f

    fun contains(
        xPx: Float,
        yPx: Float,
    ): Boolean {
        return xPx in leftPx..(leftPx + widthPx) &&
            yPx in topPx..(topPx + heightPx)
    }
}
