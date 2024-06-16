package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.widget

import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ReviewDetailsImage(
    picturesUri: ImmutableList<String>,
    currentPicturePosition: Int,
    onEmptyImageClick: () -> Unit,
    onPictureClick: () -> Unit,
    onPageChange: (pageIndex: Int) -> Unit,
) {
    if (picturesUri.isEmpty()) {
        EmptyImage(onEmptyImageClick = onEmptyImageClick)
    } else {
        PicturesCarousel(
            currentPictureIndex = currentPicturePosition,
            picturesUri = picturesUri,
            onPageChange = onPageChange,
            onPictureClick = onPictureClick,
        )
    }
}