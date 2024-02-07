package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.core.entity.CheckieReview

internal data class ReviewDetailsState(
    val reviewId: String,
    val review: Lce<CheckieReview> = Lce.initial(),
    val currentPicturePosition: Int = 0,
)