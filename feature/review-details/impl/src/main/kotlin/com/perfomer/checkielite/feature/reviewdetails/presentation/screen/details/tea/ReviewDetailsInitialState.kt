package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.feature.reviewdetails.navigation.ReviewDetailsDestination
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetails
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsState

internal fun ReviewDetailsDestination.toInitialState(): ReviewDetailsState {
    val review = initialReview?.takeIf { it.id == reviewId }

    return ReviewDetailsState(
        reviewId = reviewId,
        review = review?.let {
            Lce.Content(ReviewDetails(review = it, recommendations = emptyList()))
        } ?: Lce.initial(),
    )
}
