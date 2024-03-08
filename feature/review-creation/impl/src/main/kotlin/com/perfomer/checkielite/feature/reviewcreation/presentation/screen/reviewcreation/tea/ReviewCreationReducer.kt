package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea

import com.perfomer.checkielite.common.pure.util.next
import com.perfomer.checkielite.common.pure.util.previous
import com.perfomer.checkielite.common.pure.util.swap
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationMode
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.CreateReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.LoadReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.UpdateReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.ShowConfirmExitDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.ShowErrorDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.ReviewLoading
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.ReviewSaving
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenGallery
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenPhotoPicker
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnPhotosPick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnConfirmExitClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnPrimaryButtonClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ProductInfo
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ReviewInfo
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewDetails
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
                val isProductNameValid = state.reviewDetails.productName.isNotBlank()
                if (isProductNameValid) state { copy(currentPage = next) }
                state { copy(isProductNameValid = isProductNameValid) }
            } else {
                when (state.mode) {
                    is ReviewCreationMode.Creation -> commands(
                        CreateReview(
                            productName = state.reviewDetails.productName,
                            productBrand = state.reviewDetails.productBrand,
                            reviewText = state.reviewDetails.reviewText,
                            rating = state.reviewDetails.rating,
                            pictures = state.reviewDetails.pictures,
                        )
                    )

                    is ReviewCreationMode.Modification -> commands(
                        UpdateReview(
                            reviewId = state.reviewId,
                            productName = state.reviewDetails.productName,
                            productBrand = state.reviewDetails.productBrand,
                            reviewText = state.reviewDetails.reviewText,
                            rating = state.reviewDetails.rating,
                            pictures = state.reviewDetails.pictures,
                        )
                    )
                }

            }
        }

        is OnBackPress -> {
            when {
                state.isReviewLoadingFailed || state.isSavingFailed -> commands(Exit)
                !state.isSavingInProgress -> {
                    val previous = state.currentPage.previous()

                    if (previous != null) {
                        state { copy(currentPage = previous) }
                    } else {
                        if (state.reviewDetails != state.initialReviewDetails) {
                            effects(ShowConfirmExitDialog)
                        } else {
                            commands(Exit)
                        }
                    }
                }
            }

            Unit
        }

        is OnConfirmExitClick -> commands(Exit)
    }

    private fun reduceProductInfoUi(event: ProductInfo) = when (event) {
        is ProductInfo.OnProductNameTextInput -> state { copy(reviewDetails = reviewDetails.copy(productName = event.text)) }
        is ProductInfo.OnBrandTextInput -> state { copy(reviewDetails = reviewDetails.copy(productBrand = event.text)) }
        is ProductInfo.OnAddPictureClick -> commands(OpenPhotoPicker)
        is ProductInfo.OnPictureClick -> commands(
            OpenGallery(
                picturesUri = state.reviewDetails.pictures.map { it.uri },
                currentPicturePosition = event.position,
            )
        )

        is ProductInfo.OnPictureDeleteClick -> state {
            copy(
                reviewDetails = reviewDetails.copy(
                    pictures = reviewDetails.pictures.removeAt(event.position),
                ),
            )
        }

        is ProductInfo.OnPictureReorder -> state {
            copy(
                reviewDetails = reviewDetails.copy(
                    pictures = reviewDetails.pictures.swap(event.fromPosition, event.toPosition).toPersistentList(),
                ),
            )
        }
    }

    private fun reduceReviewInfoUi(event: ReviewInfo) = when (event) {
        is ReviewInfo.OnRatingSelect -> state { copy(reviewDetails = reviewDetails.copy(rating = event.rating)) }
        is ReviewInfo.OnReviewTextInput -> state { copy(reviewDetails = reviewDetails.copy(reviewText = event.text)) }
    }

    private fun reduceNavigation(event: ReviewCreationNavigationEvent) = when (event) {
        is OnPhotosPick -> state {
            val addedPictures = event.uris.map { uri -> CheckiePicture(uri = uri) }

            copy(
                reviewDetails = reviewDetails.copy(
                    pictures = reviewDetails.pictures.addAll(addedPictures),
                ),
            )
        }
    }

    private fun reduceReviewsCreation(event: ReviewSaving) = when (event) {
        is ReviewSaving.Started -> state { copy(isSavingInProgress = true) }
        is ReviewSaving.Succeed -> commands(ExitWithResult(ReviewCreationResult.Success))
        is ReviewSaving.Failed -> {
            state { copy(isSavingInProgress = false, isSavingFailed = true) }
            effects(ShowErrorDialog)
        }
    }

    private fun reduceReviewLoading(event: ReviewLoading) = when (event) {
        is ReviewLoading.Started -> state { copy(isReviewLoading = true) }
        is ReviewLoading.Succeed -> state {
            val initialReviewDetails = ReviewDetails(
                productName = event.review.productName,
                productBrand = event.review.productBrand.orEmpty(),
                pictures = event.review.pictures.toPersistentList(),
                reviewText = event.review.reviewText.orEmpty(),
                rating = event.review.rating,
            )

            copy(
                isReviewLoading = false,
                reviewId = event.review.id,
                initialReviewDetails = initialReviewDetails,
                reviewDetails = initialReviewDetails,
            )
        }

        is ReviewLoading.Failed -> {
            state { copy(isReviewLoading = false, isReviewLoadingFailed = true) }
            effects(ShowErrorDialog)
        }
    }
}