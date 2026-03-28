package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.widget

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.perfomer.checkielite.common.ui.cui.widget.reorder.CuiReorderableLazyRow
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.ProductInfoCarouselItemAspectRatio
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.ProductInfoCarouselSpacing
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.ProductInfoContentPadding
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.ProductInfoPhotoDragScale
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.page.productinfo.ProductInfoVisibleItems
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state.ProductInfoPageUiState
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ProductInfoPhotoCarousel(
    picturesUri: ImmutableList<ProductInfoPageUiState.Picture>,
    onAddPictureClick: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onPictureClick: (position: Int) -> Unit,
    onPictureDeleteClick: (position: Int) -> Unit,
    onPictureReorder: (pictureId: String, toPosition: Int) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val itemWidth = (
            maxWidth -
                ProductInfoContentPadding * 2 -
                ProductInfoCarouselSpacing * (ProductInfoVisibleItems - 1)
            ) / ProductInfoVisibleItems
        val itemHeight = itemWidth / ProductInfoCarouselItemAspectRatio
        val placeholderCount = maxOf(0, ProductInfoVisibleItems - picturesUri.size)
        val rowVerticalPadding = 8.dp
        val scrollAllowed = picturesUri.size > ProductInfoVisibleItems - 1

        CuiReorderableLazyRow(
            items = picturesUri,
            itemKey = ProductInfoPageUiState.Picture::id,
            onDrop = onPictureReorder,
            itemWidth = itemWidth,
            itemHeight = itemHeight,
            itemSpacing = ProductInfoCarouselSpacing,
            contentPadding = PaddingValues(
                start = ProductInfoContentPadding,
                end = ProductInfoContentPadding,
                top = rowVerticalPadding,
                bottom = rowVerticalPadding,
            ),
            rowVerticalPadding = rowVerticalPadding,
            scrollAllowed = scrollAllowed,
            shouldScrollToNewItems = true,
            leadingItemsCount = 1,
            floatingItemScale = ProductInfoPhotoDragScale,
            leadingContent = {
                item(key = "photo-actions") {
                    PhotoActionColumn(
                        onAddPictureClick = onAddPictureClick,
                        onTakePhotoClick = onTakePhotoClick,
                        modifier = Modifier
                            .width(itemWidth)
                            .height(itemHeight),
                    )
                }
            },
            trailingContent = {
                items(
                    count = placeholderCount,
                    key = { placeholderIndex -> "placeholder-$placeholderIndex" },
                ) {
                    PhotoPlaceholderCard(
                        modifier = Modifier
                            .width(itemWidth)
                            .height(itemHeight),
                    )
                }
            },
        ) { picture, index, itemState ->
            PhotoCard(
                pictureUrl = picture.uri,
                position = index,
                onClick = { if (!itemState.isInteractionActive) onPictureClick(index) },
                onDeleteClick = { onPictureDeleteClick(index) },
                isDeleteButtonVisible = !itemState.isInteractionActive && !itemState.isFloating,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
