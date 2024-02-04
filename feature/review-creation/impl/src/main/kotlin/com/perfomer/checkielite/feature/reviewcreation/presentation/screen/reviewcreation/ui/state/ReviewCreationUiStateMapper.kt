package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state

import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationState

internal class ReviewCreationUiStateMapper : UiStateMapper<ReviewCreationState, ReviewCreationUiState> {

    override fun map(state: ReviewCreationState): ReviewCreationUiState {
        val pages = enumValues<ReviewCreationPage>()
        val currentStep = pages.indexOf(state.currentPage) + 1
        val stepsCount = pages.size

        return ReviewCreationUiState(
            step = currentStep,
            stepsCount = stepsCount,
            currentPage = state.currentPage,
            primaryButtonText = if (currentStep == stepsCount) "Save" else "Next", // todo resources
            productInfoState = createProductInfoPageState(state),
            reviewInfoState = createReviewInfoPageState(state),
        )
    }

    private fun createProductInfoPageState(state: ReviewCreationState): ProductInfoPageUiState {
        return ProductInfoPageUiState(
            productName = state.productName,
            brand = state.brand,
            picturesUri = state.picturesUri,
        )
    }

    private fun createReviewInfoPageState(state: ReviewCreationState): ReviewInfoPageUiState {
        return ReviewInfoPageUiState(
            rating = state.rating,
            reviewText = state.reviewText,
        )
    }
}