package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsState
import kotlinx.collections.immutable.toPersistentList
import java.text.SimpleDateFormat
import java.util.Locale

internal class ReviewDetailsUiStateMapper : UiStateMapper<ReviewDetailsState, ReviewDetailsUiState> {

    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    override fun map(state: ReviewDetailsState): ReviewDetailsUiState {
        return when (state.review) {
            is Lce.Content -> ReviewDetailsUiState.Content(
                brandName = state.review.content.productBrand?.uppercase(),
                productName = state.review.content.productName,
                date = dateFormat.format(state.review.content.creationDate),
                rating = state.review.content.rating,
                picturesUri = state.review.content.picturesUri.toPersistentList(),
                currentPicturePosition = state.currentPicturePosition,
                reviewText = state.review.content.reviewText,
            )

            is Lce.Loading -> ReviewDetailsUiState.Loading
            is Lce.Error -> ReviewDetailsUiState.Error
        }
    }
}