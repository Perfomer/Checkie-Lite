@file:OptIn(ExperimentalFoundationApi::class)

package com.perfomer.checkielite.common.ui.cui.modifier

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.absoluteValue

fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

fun PagerState.indicatorOffsetForPage(page: Int) = 1f - offsetForPage(page).coerceIn(-1f, 1f).absoluteValue

fun Modifier.scaleHorizontalNeighbors(
    pagerState: PagerState,
    neighborScale: Float = 0.9F,
    page: Int,
): Modifier {
    return graphicsLayer {
        val pageOffset = pagerState.offsetForPage(page)
        val offScreenRight = pageOffset < 0f

        val absoluteOffset = pageOffset.absoluteValue
        val value = neighborScale + (1F - neighborScale) * (1F - absoluteOffset)

        val interpolated = FastOutLinearInEasing.transform(value)

        transformOrigin = TransformOrigin(
            pivotFractionX = if (offScreenRight) 0F else 1F,
            pivotFractionY = 0.5F,
        )

        scaleX = interpolated
        scaleY = interpolated
    }
}
