package com.perfomer.checkielite.common.ui.presentation.transition

/**
 * Keep the incoming element eligible before its lazy layout has measured. Disabling it in
 * that first composition can create a match without starting the shared bounds animation.
 * Once layout information is available, exclude missing and clipped items from the match.
 */
fun isSharedTransitionItemEligible(
    totalItemsCount: Int,
    itemOffset: Int?,
    itemSize: Int?,
    viewportStartOffset: Int,
    viewportEndOffset: Int,
): Boolean {
    if (totalItemsCount == 0) return true
    if (itemOffset == null || itemSize == null) return false

    return itemOffset >= viewportStartOffset && itemOffset + itemSize <= viewportEndOffset
}
