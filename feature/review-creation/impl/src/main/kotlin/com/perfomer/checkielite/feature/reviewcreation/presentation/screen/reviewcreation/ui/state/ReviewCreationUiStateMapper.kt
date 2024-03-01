package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state

import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationState

internal class ReviewCreationUiStateMapper : UiStateMapper<ReviewCreationState, ReviewCreationUiState> {

    override fun map(state: ReviewCreationState): ReviewCreationUiState {
        val pages = ReviewCreationPage.entries
        val currentStep = pages.indexOf(state.currentPage)
        val stepsCount = pages.size

        return ReviewCreationUiState(
            step = currentStep,
            stepsCount = stepsCount,
            currentPage = state.currentPage,
            productInfoState = createProductInfoPageState(state),
            reviewInfoState = createReviewInfoPageState(state),
            isPrimaryButtonLoading = state.isSavingInProgress,
        )
    }

    private fun createProductInfoPageState(state: ReviewCreationState): ProductInfoPageUiState {
        return ProductInfoPageUiState(
            productName = state.productName,
            brand = state.brand,
            picturesUri = state.picturesUri,
            productNameErrorText = "Product name cannot be empty".takeUnless { state.isProductNameValid },
        )
    }

    private fun createReviewInfoPageState(state: ReviewCreationState): ReviewInfoPageUiState {
        return ReviewInfoPageUiState(
            rating = state.rating,
            reviewText = state.reviewText,
        )
    }
}