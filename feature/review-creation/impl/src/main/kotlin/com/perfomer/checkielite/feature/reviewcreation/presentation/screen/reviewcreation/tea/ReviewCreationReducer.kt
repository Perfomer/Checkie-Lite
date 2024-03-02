package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea

import com.perfomer.checkielite.common.pure.util.next
import com.perfomer.checkielite.common.pure.util.previous
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationMode
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.CreateReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.LoadReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.UpdateReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.ReviewLoading
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.ReviewSaving
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenGallery
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenPhotoPicker
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnPhotoPick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnPrimaryButtonClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ProductInfo
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ReviewInfo
import kotlinx.collections.immutable.toPersistentList

internal class ReviewCreationReducer : DslReducer<ReviewCreationCommand, ReviewCreationEffect, ReviewCreationEvent, ReviewCreationState>() {

    override fun reduce(event: ReviewCreationEvent) = when (event) {
        is Initialize -> reduceInitialize()

        is ReviewCreationUiEvent -> reduceUi(event)
        is ReviewCreationNavigationEvent -> reduceNavigation(event)

        is ReviewLoading -> reduceReviewLoading(event)
        is ReviewSaving -> reduceReviewsCreation(event)
    }

    private fun reduceInitialize() {
        val mode = state.mode as? ReviewCreationMode.Modification ?: return
        commands(LoadReview(mode.reviewId))
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
                when (state.mode) {
                    is ReviewCreationMode.Creation -> commands(
                        CreateReview(
                            productName = state.productName,
                            productBrand = state.productBrand,
                            reviewText = state.reviewText,
                            rating = state.rating,
                            picturesUri = state.picturesUri,
                        )
                    )

                    is ReviewCreationMode.Modification -> commands(
                        UpdateReview(
                            reviewId = state.reviewId,
                            productName = state.productName,
                            productBrand = state.productBrand,
                            reviewText = state.reviewText,
                            rating = state.rating,
                            picturesUri = state.picturesUri,
                        )
                    )
                }

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
        is ProductInfo.OnProductNameTextInput -> state { copy(productName = event.text) }
        is ProductInfo.OnBrandTextInput -> state { copy(productBrand = event.text) }
        is ProductInfo.OnAddPictureClick -> commands(OpenPhotoPicker)
        is ProductInfo.OnPictureClick -> commands(
            OpenGallery(
                picturesUri = state.picturesUri,
                currentPicturePosition = state.picturesUri.indexOf(event.pictureUri).coerceAtLeast(0),
            )
        )

        is ProductInfo.OnPictureDeleteClick -> state { copy(picturesUri = picturesUri.remove(event.pictureUri)) }
    }

    private fun reduceReviewInfoUi(event: ReviewInfo) = when (event) {
        is ReviewInfo.OnRatingSelect -> state { copy(rating = event.rating) }
        is ReviewInfo.OnReviewTextInput -> state { copy(reviewText = event.text) }
    }

    private fun reduceNavigation(event: ReviewCreationNavigationEvent) = when (event) {
        is OnPhotoPick -> state { copy(picturesUri = picturesUri.add(event.uri)) }
    }

    private fun reduceReviewsCreation(event: ReviewSaving) = when (event) {
        is ReviewSaving.Started -> state { copy(isSavingInProgress = true) }
        is ReviewSaving.Succeed -> commands(ExitWithResult(ReviewCreationResult.Success))
        is ReviewSaving.Failed -> TODO()
    }

    private fun reduceReviewLoading(event: ReviewLoading) = when (event) {
        is ReviewLoading.Started -> state { copy(isReviewLoading = true) }
        is ReviewLoading.Succeed -> state {
            copy(
                reviewId = event.review.id,
                productName = event.review.productName,
                productBrand = event.review.productBrand.orEmpty(),
                picturesUri = event.review.picturesUri.toPersistentList(),
                reviewText = event.review.reviewText.orEmpty(),
                rating = event.review.rating,
            )
        }

        is ReviewLoading.Failed -> TODO()
    }
}