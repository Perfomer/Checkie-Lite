package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.ui.state

import android.content.Context
import com.perfomer.checkielite.common.tea.component.UiStateMapper
import com.perfomer.checkielite.feature.reviewcreation.R
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationState

internal class ReviewCreationUiStateMapper(
    private val context: Context,
) : UiStateMapper<ReviewCreationState, ReviewCreationUiState> {

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
            productName = state.reviewDetails.productName,
            brand = state.reviewDetails.productBrand,
            picturesUri = state.reviewDetails.picturesUri,
            productNameErrorText = context.getString(R.string.reviewcreation_productinfo_field_product_error_empty)
                .takeUnless { state.isProductNameValid },
        )
    }

    private fun createReviewInfoPageState(state: ReviewCreationState): ReviewInfoPageUiState {
        return ReviewInfoPageUiState(
            rating = state.reviewDetails.rating,
            reviewText = state.reviewDetails.reviewText,
        )
    }
}