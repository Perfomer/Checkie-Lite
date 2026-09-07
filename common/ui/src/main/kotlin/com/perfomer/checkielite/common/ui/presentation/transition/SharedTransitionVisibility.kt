package com.perfomer.checkielite.common.ui.presentation.transition

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationContent

/** Shares content only while its lazy-list item is fully visible. */
@Composable
fun SharedNavigationLazyListItem(
    id: Any?,
    listState: LazyListState,
    itemKey: Any? = id,
    viewportStartOffset: Int = 0,
    content: @Composable () -> Unit,
) {
    SharedNavigationContent(
        id = id,
        isEnabled = {
            val layout = listState.layoutInfo
            val item = layout.visibleItemsInfo.firstOrNull { it.key == itemKey }
            isSharedTransitionItemEligible(
                totalItemsCount = layout.totalItemsCount,
                itemOffset = item?.offset,
                itemSize = item?.size,
                viewportStartOffset = viewportStartOffset,
                viewportEndOffset = layout.viewportEndOffset,
            )
        },
        content = content,
    )
}

/**
 * Keep the incoming element eligible before its lazy layout has measured. Disabling it in
 * that first composition can create a match without starting the shared bounds animation.
 * Once layout information is available, exclude missing and clipped items from the match.
 */
internal fun isSharedTransitionItemEligible(
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
