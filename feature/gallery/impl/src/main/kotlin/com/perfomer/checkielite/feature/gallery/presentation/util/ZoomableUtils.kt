package com.perfomer.checkielite.feature.gallery.presentation.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import net.engawapg.lib.zoomable.ZoomState

/**
 * Reset zoom state when the page is moved out of the window.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Modifier.resetZoomOnMoveOut(
    currentPage: Int,
    zoomState: ZoomState,
    pagerState: PagerState,
): Modifier {
    val isVisible = currentPage == pagerState.settledPage
    LaunchedEffect(isVisible) {
        if (!isVisible) zoomState.reset()
    }

    return this
}