package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core

import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage

internal data class ReviewCreationState(
    val currentPage: ReviewCreationPage = ReviewCreationPage.PRODUCT_INFO,
)