package com.perfomer.checkielite.feature.reviewdetails.navigation

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.core.navigation.NavigationData
import com.perfomer.checkielite.core.navigation.NavigationDataKey
import com.perfomer.checkielite.core.navigation.Router
import com.perfomer.checkielite.core.navigation.navigationData

private val InitialReviewKey = NavigationDataKey<CheckieReview>()

/** Opens review details with optional content for the first rendered frame. */
fun Router.navigateToReviewDetails(
    reviewId: String,
    initialReview: CheckieReview? = null,
) {
    navigate(
        destination = ReviewDetailsDestination(reviewId = reviewId),
        navigationData = reviewDetailsNavigationData(initialReview),
    )
}

internal fun reviewDetailsNavigationData(initialReview: CheckieReview?): NavigationData = navigationData {
    initialReview?.let { put(InitialReviewKey, it) }
}

fun NavigationData.reviewDetailsInitialReview(): CheckieReview? = this[InitialReviewKey]
