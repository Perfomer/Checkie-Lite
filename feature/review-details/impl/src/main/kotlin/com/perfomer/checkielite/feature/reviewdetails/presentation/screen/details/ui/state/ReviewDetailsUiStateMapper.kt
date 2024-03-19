package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.ui.state

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsState
import kotlinx.collections.immutable.toPersistentList
import java.text.SimpleDateFormat
import java.util.Locale

internal class ReviewDetailsUiStateMapper : UiStateMapper<ReviewDetailsState, ReviewDetailsUiState> {

    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    override fun map(state: ReviewDetailsState): ReviewDetailsUiState {
        return when (state.review) {
            is Lce.Content -> {
                val content = state.review.content
                val review = content.review

                ReviewDetailsUiState.Content(
                    brandName = review.productBrand?.uppercase(),
                    productName = review.productName,
                    date = dateFormat.format(review.creationDate),
                    rating = review.rating,
                    picturesUri = review.pictures.map { it.uri }.toPersistentList(),
                    currentPicturePosition = state.currentPicturePosition,
                    reviewText = review.reviewText,
                    isMenuAvailable = !review.isSyncing,
                    recommendations = content.recommendations.map { it.toRecommendation() }.toPersistentList(),
                )
            }

            is Lce.Loading -> ReviewDetailsUiState.Loading
            is Lce.Error -> ReviewDetailsUiState.Error
        }
    }

    private fun CheckieReview.toRecommendation(): RecommendedReview {
        return RecommendedReview(
            reviewId = id,
            brandName = productBrand?.uppercase(),
            productName = productName,
            pictureUri = pictures.firstOrNull()?.uri,
            rating = rating,
            isSyncing = isSyncing,
        )
    }
}