package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core

import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationMode
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import kotlinx.collections.immutable.PersistentList

internal data class ReviewCreationState(
    val mode: ReviewCreationMode,
    val currentPage: ReviewCreationPage = mode.initialPage,

    val initialReviewDetails: ReviewDetails = ReviewDetails(),
    val reviewDetails: ReviewDetails = initialReviewDetails,
    val isProductNameValid: Boolean = true,

    val isReviewLoading: Boolean = false,
    val isSavingInProgress: Boolean = false,
)

internal data class ReviewDetails(
    val reviewId: String = "",
    val productName: String = "",
    val productBrand: String = "",
    val picturesUri: PersistentList<String> = emptyPersistentList(),

    val rating: Int = 5,
    val reviewText: String = "",
)