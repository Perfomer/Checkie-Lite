package com.perfomer.checkielite.feature.reviewdetails.navigation

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.core.navigation.InitialContent

/** Optional snapshot for the first frame; the repository remains the source of truth. */
data class ReviewDetailsInitialContent(
    val review: CheckieReview,
) : InitialContent<ReviewDetailsDestination>
