package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.CreateReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.ReviewCreation
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenPhotoPicker
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnPhotoPick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnPrimaryButtonClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ProductInfo
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ReviewInfo
import com.perfomer.checkielite.common.pure.util.next
import com.perfomer.checkielite.common.pure.util.previous
import java.util.Date
import java.util.UUID

internal class ReviewCreationReducer : DslReducer<ReviewCreationCommand, ReviewCreationEffect, ReviewCreationEvent, ReviewCreationState>() {

    override fun reduce(event: ReviewCreationEvent) = when (event) {
        is Initialize -> Unit

        is ReviewCreationUiEvent -> reduceUi(event)
        is ReviewCreationNavigationEvent -> reduceNavigation(event)
        is ReviewCreation -> reduceReviewsCreation(event)
    }

    private fun reduceUi(event: ReviewCreationUiEvent) = when (event) {
        is ProductInfo -> reduceProductInfoUi(event)
        is ReviewInfo -> reduceReviewInfoUi(event)
        is OnPrimaryButtonClick -> {
            val next = state.currentPage.next()

            if (next != null) {
                val isProductNameValid = state.productName.isNotBlank()
                if (isProductNameValid) state { copy(currentPage = next) }
                state { copy(isProductNameValid = isProductNameValid) }
            } else {
                commands(
                    CreateReview(
                        CheckieReview(
                            id = UUID.randomUUID().toString(),
                            productName = state.productName,
                            productBrand = state.brand.takeIf { it.isNotBlank() },
                            rating = state.rating,
                            picturesUri = state.picturesUri,
                            reviewText = state.reviewText.takeIf { it.isNotBlank() },
                            creationDate = Date(),
                        )
                    )
                )
            }
        }

        is OnBackPress -> {
            val previous = state.currentPage.previous()

            if (previous != null) {
                state { copy(currentPage = previous) }
            } else {
                commands(Exit)
            }
        }
    }

    private fun reduceProductInfoUi(event: ProductInfo) = when (event) {
        is ProductInfo.OnAddPictureClick -> commands(OpenPhotoPicker)
        is ProductInfo.OnBrandTextInput -> state { copy(brand = event.text) }
        is ProductInfo.OnPictureDeleteClick -> state { copy(picturesUri = picturesUri.remove(event.pictureUri)) }
        is ProductInfo.OnProductNameTextInput -> state { copy(productName = event.text) }
    }

    private fun reduceReviewInfoUi(event: ReviewInfo) = when (event) {
        is ReviewInfo.OnRatingSelect -> state { copy(rating = event.rating) }
        is ReviewInfo.OnReviewTextInput -> state { copy(reviewText = event.text) }
    }

    private fun reduceNavigation(event: ReviewCreationNavigationEvent) = when (event) {
        is OnPhotoPick -> state { copy(picturesUri = picturesUri.add(event.uri)) }
    }

    private fun reduceReviewsCreation(event: ReviewCreation) = when (event) {
        is ReviewCreation.Started -> state { copy(isSavingInProgress = true) }
        is ReviewCreation.Succeed -> commands(ExitWithResult(ReviewCreationResult.Success))
    }
}