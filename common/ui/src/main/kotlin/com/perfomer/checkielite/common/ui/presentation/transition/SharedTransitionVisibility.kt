package com.perfomer.checkielite.common.ui.presentation.transition

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import com.perfomer.checkielite.core.navigation.transition.SharedContentGroup
import com.perfomer.checkielite.core.navigation.transition.SharedNavigationContent

/**
 * Shares content only while its lazy-list item is fully visible.
 * [viewportStartOffset] is the unobscured start measured from the list's outer edge (e.g. a toolbar).
 * When omitted, the start of the padded content is used.
 */
@Composable
fun SharedNavigationLazyListItem(
    id: Any?,
    listState: LazyListState,
    group: SharedContentGroup? = null,
    itemKey: Any? = id,
    viewportStartOffset: Int? = null,
    isEnabled: () -> Boolean = { true },
    content: @Composable () -> Unit,
) {
    SharedNavigationContent(
        id = id,
        group = group,
        isEnabled = {
            val layout = listState.layoutInfo
            val item = layout.visibleItemsInfo.firstOrNull { it.key == itemKey }
            isEnabled() && isSharedTransitionItemEligible(
                totalItemsCount = layout.totalItemsCount,
                itemOffset = item?.offset,
                itemSize = item?.size,
                viewportStartOffset = viewportStartOffset,
                viewportEndOffset = layout.viewportEndOffset,
                layoutViewportStartOffset = layout.viewportStartOffset,
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
    viewportStartOffset: Int?,
    viewportEndOffset: Int,
    layoutViewportStartOffset: Int = 0,
): Boolean {
    if (totalItemsCount == 0) return true
    if (itemOffset == null || itemSize == null) return false

    // Lazy item offsets exclude beforeContentPadding; the outer viewport starts at its negative.
    val visibleStart = viewportStartOffset?.plus(layoutViewportStartOffset) ?: 0
    return itemOffset >= visibleStart && itemOffset + itemSize <= viewportEndOffset
}
