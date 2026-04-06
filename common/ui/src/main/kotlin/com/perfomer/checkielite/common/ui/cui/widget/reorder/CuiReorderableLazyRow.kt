package com.perfomer.checkielite.common.ui.cui.widget.reorder

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlin.math.abs
import kotlin.math.roundToInt

private const val CuiReorderableLazyRowFloatingItemScale = 1.04F

@Immutable
data class CuiReorderableLazyRowItemState(
    val isDraggedItem: Boolean,
    val isFloating: Boolean,
    val isDragging: Boolean,
    val isSettling: Boolean,
    val isInteractionActive: Boolean,
)

@Composable
fun <T, K : Any> CuiReorderableLazyRow(
    items: List<T>,
    itemKey: (T) -> K,
    onDrop: (itemKey: K, toPosition: Int) -> Unit,
    itemSize: DpSize,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    reorderState: CuiReorderableLazyRowState<K>? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemSpacing: Dp = 0.dp,
    scrollAllowed: Boolean = true,
    floatingItemScale: Float = CuiReorderableLazyRowFloatingItemScale,
    itemPlacementSpec: FiniteAnimationSpec<IntOffset> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow,
    ),
    leadingContent: LazyListScope.() -> Unit = {},
    trailingContent: LazyListScope.() -> Unit = {},
    itemContent: @Composable BoxScope.(item: T, index: Int, itemState: CuiReorderableLazyRowItemState) -> Unit,
) {
    val resolvedReorderState = reorderState ?: rememberCuiReorderableLazyRowState(itemSpacing = itemSpacing)
    val density = LocalDensity.current
    val hapticFeedback = LocalHapticFeedback.current
    val layoutDirection = LocalLayoutDirection.current

    var reorderableItems by remember { mutableStateOf(items) }
    var isAwaitingExternalSync by remember { mutableStateOf(false) }
    var containerLeftInRoot by remember { mutableFloatStateOf(0F) }
    var containerTopInRoot by remember { mutableFloatStateOf(0F) }

    val isInteractionActive = resolvedReorderState.isDragging || resolvedReorderState.isSettling
    val currentReorderableItems by rememberUpdatedState(newValue = reorderableItems)
    val currentOnDrop by rememberUpdatedState(newValue = onDrop)
    val currentItemKey by rememberUpdatedState(newValue = itemKey)
    val currentIsInteractionActive by rememberUpdatedState(newValue = isInteractionActive)
    val currentContainerLeftInRoot by rememberUpdatedState(newValue = containerLeftInRoot)
    val currentContainerTopInRoot by rememberUpdatedState(newValue = containerTopInRoot)

    UpdateEffect(items) {
        val newKeyPool = items.mapTo(mutableSetOf(), itemKey)
        resolvedReorderState.retainMeasuredItems(newKeyPool)

        when {
            !reorderableItems.hasSameItemPool(
                other = items,
                keySelector = itemKey,
            ) -> {
                reorderableItems = items
                isAwaitingExternalSync = false
                resolvedReorderState.resetImmediately()
            }

            !isInteractionActive -> {
                when {
                    reorderableItems == items -> {
                        isAwaitingExternalSync = false
                    }

                    !isAwaitingExternalSync && reorderableItems != items -> {
                        reorderableItems = items
                    }
                }
            }
        }
    }

    UpdateEffect(scrollAllowed) {
        if (!scrollAllowed) {
            resolvedReorderState.stopAutoScroll()
            listState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(
        resolvedReorderState.isDragging,
        listState,
        scrollAllowed,
    ) {
        if (!resolvedReorderState.isDragging || !scrollAllowed) {
            resolvedReorderState.stopAutoScroll()
            return@LaunchedEffect
        }

        var previousFrameNanos = 0L

        while (resolvedReorderState.isDragging && currentCoroutineContext().isActive) {
            val frameNanos = withFrameNanos { it }
            val frameDeltaSeconds = if (previousFrameNanos == 0L) {
                1F / 60F
            } else {
                ((frameNanos - previousFrameNanos) / 1_000_000_000f)
                    .coerceAtLeast(1F / 120F)
            }
            previousFrameNanos = frameNanos

            val autoScrollDelta = resolvedReorderState.autoScrollVelocityPxPerSecond * frameDeltaSeconds
            if (autoScrollDelta == 0F) continue

            val consumedScroll = listState.scrollBy(autoScrollDelta)
            if (consumedScroll == 0F) continue

            val move = resolvedReorderState.onScroll(
                itemKeys = currentReorderableItems.map(currentItemKey),
            )
            if (move != null) {
                reorderableItems = reorderableItems.move(
                    fromIndex = move.fromIndex,
                    toIndex = move.toIndex,
                )
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                containerLeftInRoot = coordinates.positionInRoot().x
                containerTopInRoot = coordinates.positionInRoot().y
            }
    ) {
        val contentStartPx = with(density) {
            contentPadding.calculateStartPadding(layoutDirection).toPx()
        }
        val contentEndPx = with(density) {
            maxWidth.toPx() - contentPadding.calculateEndPadding(layoutDirection).toPx()
        }
        val floatingItemKey = resolvedReorderState.floatingItemKey
        val floatingItem = remember(floatingItemKey, reorderableItems, itemKey) {
            reorderableItems.firstOrNull { item -> itemKey(item) == floatingItemKey }
        }
        val floatingItemPosition = remember(floatingItemKey, reorderableItems, itemKey) {
            reorderableItems.indexOfFirst { item -> itemKey(item) == floatingItemKey }
        }
        val floatingItemLayout = floatingItemKey?.let(resolvedReorderState::getItemLayout)
        val floatingItemOffsetX = remember { Animatable(0f) }
        val animatedFloatingItemScale by animateFloatAsState(
            targetValue = if (floatingItemKey != null) floatingItemScale else 1f,
            label = "CuiReorderableLazyRowFloatingItemScale",
        )
        val floatingItemLeftPx = if (resolvedReorderState.isDragging) {
            resolvedReorderState.overlayLeftPx
        } else {
            floatingItemOffsetX.value
        }

        resolvedReorderState.updateViewport(
            contentStartPx = containerLeftInRoot + contentStartPx,
            contentEndPx = containerLeftInRoot + contentEndPx,
        )

        LaunchedEffect(
            resolvedReorderState.isDragging,
            resolvedReorderState.overlayLeftPx,
            resolvedReorderState.isSettling,
            resolvedReorderState.settlingFromLeftPx,
            resolvedReorderState.settlingToLeftPx,
        ) {
            when {
                resolvedReorderState.isDragging -> {
                    floatingItemOffsetX.snapTo(resolvedReorderState.overlayLeftPx)
                }

                resolvedReorderState.isSettling -> {
                    floatingItemOffsetX.snapTo(resolvedReorderState.settlingFromLeftPx)
                    val settlingDistancePx = abs(
                        resolvedReorderState.settlingToLeftPx - resolvedReorderState.settlingFromLeftPx,
                    )
                    if (settlingDistancePx <= 1f) {
                        floatingItemOffsetX.snapTo(resolvedReorderState.settlingToLeftPx)
                    } else {
                        floatingItemOffsetX.animateTo(
                            targetValue = resolvedReorderState.settlingToLeftPx,
                            animationSpec = tween(
                                durationMillis = 140,
                                easing = FastOutSlowInEasing,
                            ),
                        )
                    }
                    resolvedReorderState.onSettlingFinished()
                }

                else -> {
                    if (floatingItemOffsetX.value != 0f) {
                        floatingItemOffsetX.snapTo(0f)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { offset ->
                            if (
                                currentReorderableItems.size < 2 ||
                                currentIsInteractionActive
                            ) {
                                return@detectDragGesturesAfterLongPress
                            }

                            val currentItemKeys = currentReorderableItems.map(currentItemKey)
                            val itemIndex = resolvedReorderState.findItemIndexAtPosition(
                                itemKeys = currentItemKeys,
                                touchX = currentContainerLeftInRoot + offset.x,
                                touchY = currentContainerTopInRoot + offset.y,
                            ) ?: return@detectDragGesturesAfterLongPress

                            val didStart = resolvedReorderState.startDrag(
                                itemKey = currentItemKeys[itemIndex],
                                itemIndex = itemIndex,
                            )
                            if (!didStart) return@detectDragGesturesAfterLongPress

                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onDragEnd = {
                            resolvedReorderState.finishDrag(
                                itemKeys = currentReorderableItems.map(currentItemKey),
                            )?.let { dropResult ->
                                isAwaitingExternalSync = true
                                currentOnDrop(dropResult.itemKey, dropResult.toPosition)
                            }
                        },
                        onDragCancel = {
                            resolvedReorderState.cancelDrag(
                                itemKeys = currentReorderableItems.map(currentItemKey),
                            )?.let { dropResult ->
                                isAwaitingExternalSync = true
                                currentOnDrop(dropResult.itemKey, dropResult.toPosition)
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()

                            val move = resolvedReorderState.onDrag(
                                itemKeys = currentReorderableItems.map(currentItemKey),
                                dragDeltaX = dragAmount.x,
                            )

                            if (move != null) {
                                reorderableItems = reorderableItems.move(
                                    fromIndex = move.fromIndex,
                                    toIndex = move.toIndex,
                                )
                            }
                        },
                    )
                }
        ) {
            LazyRow(
                state = listState,
                userScrollEnabled = !isInteractionActive && scrollAllowed,
                contentPadding = contentPadding,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(itemSpacing),
                modifier = Modifier.fillMaxWidth(),
            ) {
                leadingContent()

                itemsIndexed(
                    items = reorderableItems,
                    key = { _, item -> itemKey(item) },
                ) { index, item ->
                    val currentItemKeyValue = itemKey(item)
                    val isHidden = currentItemKeyValue == floatingItemKey
                    val itemState = CuiReorderableLazyRowItemState(
                        isDraggedItem = currentItemKeyValue == resolvedReorderState.draggedItemKey ||
                            currentItemKeyValue == resolvedReorderState.settlingItemKey,
                        isFloating = false,
                        isDragging = currentItemKeyValue == resolvedReorderState.draggedItemKey,
                        isSettling = currentItemKeyValue == resolvedReorderState.settlingItemKey,
                        isInteractionActive = isInteractionActive,
                    )
                    val itemModifier = if (isHidden) {
                        Modifier
                    } else {
                        Modifier.animateItem(
                            fadeInSpec = null,
                            fadeOutSpec = null,
                            placementSpec = itemPlacementSpec,
                        )
                    }

                    Box(
                        modifier = itemModifier
                            .size(itemSize)
                            .onGloballyPositioned { coordinates ->
                                resolvedReorderState.onItemMeasured(
                                    itemKey = currentItemKeyValue,
                                    leftPx = coordinates.positionInRoot().x,
                                    topPx = coordinates.positionInRoot().y,
                                    widthPx = coordinates.size.width.toFloat(),
                                    heightPx = coordinates.size.height.toFloat(),
                                )
                            }
                            .graphicsLayer {
                                alpha = if (isHidden) 0f else 1f
                            }
                    ) {
                        itemContent(
                            item,
                            index,
                            itemState,
                        )
                    }
                }

                trailingContent()
            }

            if (floatingItem != null && floatingItemPosition != -1 && floatingItemLayout != null) {
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = (floatingItemLeftPx - containerLeftInRoot).roundToInt(),
                                y = (floatingItemLayout.topPx - containerTopInRoot).roundToInt(),
                            )
                        }
                        .width(with(density) { floatingItemLayout.widthPx.toDp() })
                        .height(with(density) { floatingItemLayout.heightPx.toDp() })
                        .zIndex(2f)
                        .graphicsLayer {
                            scaleX = animatedFloatingItemScale
                            scaleY = animatedFloatingItemScale
                        }
                ) {
                    itemContent(
                        floatingItem,
                        floatingItemPosition,
                        CuiReorderableLazyRowItemState(
                            isDraggedItem = true,
                            isFloating = true,
                            isDragging = resolvedReorderState.isDragging,
                            isSettling = resolvedReorderState.isSettling,
                            isInteractionActive = true,
                        ),
                    )
                }
            }
        }
    }
}

private fun <T> List<T>.move(
    fromIndex: Int,
    toIndex: Int,
): List<T> {
    if (fromIndex == toIndex || fromIndex !in indices || toIndex !in indices) {
        return this
    }

    return toMutableList().apply {
        add(toIndex, removeAt(fromIndex))
    }
}

private fun <T, K : Any> List<T>.hasSameItemPool(
    other: List<T>,
    keySelector: (T) -> K,
): Boolean {
    if (size != other.size) return false

    return map(keySelector).toSet() == other.map(keySelector).toSet()
}
