package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.core.domain.entity.review.CheckieReview

internal data class ReviewDetailsState(
    val reviewId: String,
    val review: Lce<ReviewDetails> = Lce.initial(),
    val currentPicturePosition: Int = 0,
)

internal data class ReviewDetails(
    val review: CheckieReview,
    val recommendations: List<CheckieReview>,
)