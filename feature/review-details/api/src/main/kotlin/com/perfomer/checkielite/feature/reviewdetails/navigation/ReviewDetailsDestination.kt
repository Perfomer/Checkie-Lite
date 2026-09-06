package com.perfomer.checkielite.feature.reviewdetails.navigation

import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.core.navigation.Destination
import com.perfomer.checkielite.core.navigation.SharedTransitionDestination
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ReviewDetailsDestination(
    val reviewId: String,
    // The current card content lets the shared image participate from the first frame.
    // Restored destinations reload from the repository instead of retaining stale snapshots.
    @Transient val initialReview: CheckieReview? = null,
) : Destination(), SharedTransitionDestination {

    // The transient snapshot must not change navigation identity before/after restoration.
    override fun equals(other: Any?): Boolean =
        other is ReviewDetailsDestination && reviewId == other.reviewId

    override fun hashCode(): Int = reviewId.hashCode()

    override val sharedTransitionGroup: String
        get() = "review"
}
