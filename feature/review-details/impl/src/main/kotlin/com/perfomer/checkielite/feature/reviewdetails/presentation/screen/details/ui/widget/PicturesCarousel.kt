package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.effect.UpdateEffect
import com.perfomer.checkielite.common.ui.cui.modifier.scaleHorizontalNeighbors
import com.perfomer.checkielite.common.ui.cui.modifier.softShadow
import com.perfomer.checkielite.common.ui.cui.widget.pager.CuiHorizontalPagerIndicator
import com.perfomer.checkielite.common.ui.presentation.transition.SharedImage
import com.perfomer.checkielite.common.ui.theme.LocalCuiPalette
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun PicturesCarousel(
    reviewId: String,
    currentPictureIndex: Int,
    picturesUri: ImmutableList<String>,
    onPageChange: (pageIndex: Int) -> Unit,
    onPictureClick: () -> Unit,
    isTransitionEnabled: () -> Boolean,
) {
    val pagerState = rememberPagerState(
        initialPage = currentPictureIndex,
        pageCount = { picturesUri.size },
    )
    UpdateEffect(pagerState.currentPage) { onPageChange(pagerState.currentPage) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            key = { picturesUri[it] },
            pageSpacing = 12.dp,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        ) { page ->
            SharedImage(
                contentId = reviewId,
                imageUri = picturesUri[page],
                cornerRadius = 24.dp,
                otherCornerRadius = 16.dp,
                // Only the visible cover participates in the transition from the main list.
                isTransitionEnabled = {
                    page == 0 && pagerState.currentPage == page &&
                        pagerState.currentPageOffsetFraction == 0F && isTransitionEnabled()
                },
                modifier = Modifier
                    .scaleHorizontalNeighbors(pagerState = pagerState, page = page)
                    .fillMaxWidth()
                    .aspectRatio(1F)
                    .softShadow(shape = RoundedCornerShape(24.dp))
                    .clickable(onClick = onPictureClick)
            )
        }
        if (pagerState.pageCount > 1) {
            CuiHorizontalPagerIndicator(
                state = pagerState,
                selectedWidth = 20.dp,
                defaultWidth = 6.dp,
                selectedColor = LocalCuiPalette.current.BackgroundAccentPrimary,
                defaultColor = LocalCuiPalette.current.BackgroundTertiary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}
