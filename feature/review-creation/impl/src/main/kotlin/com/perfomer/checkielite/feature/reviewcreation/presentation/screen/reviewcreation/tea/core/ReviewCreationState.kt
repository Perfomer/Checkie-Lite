package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core

import com.perfomer.checkielite.common.pure.util.emptyPersistentList
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationMode
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import kotlinx.collections.immutable.PersistentList

internal data class ReviewCreationState(
    val mode: ReviewCreationMode,
    val currentPage: ReviewCreationPage = mode.initialPage,

    val reviewId: String = "",
    val initialReviewDetails: ReviewDetails = ReviewDetails(),
    val reviewDetails: ReviewDetails = initialReviewDetails,
    val isProductNameValid: Boolean = true,

    val isReviewLoading: Boolean = false,
    val isReviewLoadingFailed: Boolean = false,

    val isSavingInProgress: Boolean = false,
    val isSavingFailed: Boolean = false,
)

internal data class ReviewDetails(
    val productName: String = "",
    val productBrand: String = "",
    val picturesUri: PersistentList<String> = emptyPersistentList(),

    val rating: Int = 5,
    val reviewText: String = "",
)