package com.perfomer.checkielite.feature.reviewdetails.presentation.navigation

import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.core.navigation.InitialContent as NavigationInitialContent
import com.perfomer.checkielite.core.navigation.transition.SharedContentGroup
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDetailsDestination(
    val reviewId: String,
) : Destination() {

    data class InitialContent(
        val review: CheckieReview,
    ) : NavigationInitialContent<ReviewDetailsDestination>

    data object ReviewContent : SharedContentGroup {
        val ReviewListItem = role()
        val ReviewPage = role()
        val ReviewRecommendation = role()
    }
}
