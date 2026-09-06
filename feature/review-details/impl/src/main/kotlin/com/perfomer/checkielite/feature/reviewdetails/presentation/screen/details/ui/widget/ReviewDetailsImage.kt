package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ReviewDetailsImage(
    reviewId: String,
    picturesUri: ImmutableList<String>,
    currentPicturePosition: Int,
    onEmptyImageClick: () -> Unit,
    onPictureClick: () -> Unit,
    onPageChange: (pageIndex: Int) -> Unit,
    isTransitionEnabled: () -> Boolean,
) {
    if (picturesUri.isEmpty()) {
        EmptyImage(onEmptyImageClick = onEmptyImageClick)
    } else {
        PicturesCarousel(
            reviewId = reviewId,
            currentPictureIndex = currentPicturePosition,
            picturesUri = picturesUri,
            onPageChange = onPageChange,
            onPictureClick = onPictureClick,
            isTransitionEnabled = isTransitionEnabled,
        )
    }
}
