package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea

import com.perfomer.checkielite.common.pure.util.next
import com.perfomer.checkielite.common.pure.util.previous
import com.perfomer.checkielite.common.pure.util.swap
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationMode
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagCreationMode
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.CreateReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.LoadReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.LoadTags
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.SearchBrands
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.UpdateReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.CloseKeyboard
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.ShowConfirmExitDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.ShowErrorDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.BrandsSearchComplete
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.ReviewLoading
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.ReviewSaving
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.TagsLoading
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenGallery
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenPhotoPicker
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenTagCreation
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnPhotosPick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnTagCreated
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnTagDeleted
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnConfirmExitClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnPrimaryButtonClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ProductInfo
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ReviewInfo
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.Tags
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewDetails
import kotlinx.collections.immutable.toPersistentList

internal class ReviewCreationReducer : DslReducer<ReviewCreationCommand, ReviewCreationEffect, ReviewCreationEvent, ReviewCreationState>() {

    override fun reduce(event: ReviewCreationEvent) = when (event) {
        is Initialize -> reduceInitialize()

        is ReviewCreationUiEvent -> reduceUi(event)
        is ReviewCreationNavigationEvent -> reduceNavigation(event)

        is ReviewLoading -> reduceReviewLoading(event)
        is ReviewSaving -> reduceReviewsCreation(event)
        is TagsLoading -> reduceTagsLoading(event)

        is BrandsSearchComplete -> state { copy(suggestedBrands = event.brands) }
    }

    private fun reduceInitialize() {
        val modificationMode = state.mode as? ReviewCreationMode.Modification
        if (modificationMode != null) commands(LoadReview(modificationMode.reviewId))

        commands(LoadTags())
    }

    private fun reduceUi(event: ReviewCreationUiEvent) = when (event) {
        is ProductInfo -> reduceProductInfoUi(event)
        is Tags -> reduceTagsUi(event)
        is ReviewInfo -> reduceReviewInfoUi(event)
        is OnPrimaryButtonClick -> {
            val next = state.currentPage.next()

            if (next != null) {
                val isProductNameValid = state.reviewDetails.productName.isNotBlank()
                if (isProductNameValid) state { copy(currentPage = next) }
                state { copy(isProductNameValid = isProductNameValid) }
            } else {
                when (val mode = state.mode) {
                    is ReviewCreationMode.Creation -> commands(
                        CreateReview(
                            productName = state.reviewDetails.productName.trim(),
                            productBrand = state.reviewDetails.productBrand.trim(),
                            comment = state.reviewDetails.comment.trim(),
                            advantages = state.reviewDetails.advantages.trim(),
                            disadvantages = state.reviewDetails.disadvantages.trim(),
                            rating = state.reviewDetails.rating,
                            pictures = state.reviewDetails.pictures,
                            tagsIds = state.reviewDetails.tagsIds,
                        )
                    )

                    is ReviewCreationMode.Modification -> commands(
                        UpdateReview(
                            reviewId = mode.reviewId,
                            productName = state.reviewDetails.productName.trim(),
                            productBrand = state.reviewDetails.productBrand.trim(),
                            comment = state.reviewDetails.comment.trim(),
                            advantages = state.reviewDetails.advantages.trim(),
                            disadvantages = state.reviewDetails.disadvantages.trim(),
                            rating = state.reviewDetails.rating,
                            pictures = state.reviewDetails.pictures,
                            tagsIds = state.reviewDetails.tagsIds,
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
        is ProductInfo.OnBrandTextInput -> {
            state { copy(reviewDetails = reviewDetails.copy(productBrand = event.text)) }

            if (event.text.isBlank()) state { copy(suggestedBrands = emptyList()) }
            else commands(SearchBrands(event.text.trim()))
        }

        is ProductInfo.OnAddPictureClick -> commands(OpenPhotoPicker)

        is ProductInfo.OnPictureClick -> {
            effects(CloseKeyboard)

            commands(
                OpenGallery(
                    picturesUri = state.reviewDetails.pictures.map { it.uri },
                    currentPicturePosition = event.position,
                )
            )
        }

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

    private fun reduceTagsUi(event: Tags) = when (event) {
        is Tags.OnCreateTagClick -> {
            commands(OpenTagCreation(TagCreationMode.Creation(state.tagsSearchQuery.trim())))
        }

        is Tags.OnSearchQueryClearClick -> onTagsSearchQueryInput("")
        is Tags.OnSearchQueryInput -> onTagsSearchQueryInput(event.query)
        is Tags.OnTagClick -> {
            val isTagSelected = state.reviewDetails.tagsIds.any { it == event.tagId }
            state {
                copy(
                    reviewDetails = reviewDetails.copy(
                        tagsIds = if (isTagSelected) {
                            reviewDetails.tagsIds - event.tagId
                        } else {
                            reviewDetails.tagsIds + event.tagId
                        }
                    )
                )
            }
        }

        is Tags.OnTagLongClick -> commands(OpenTagCreation(TagCreationMode.Modification(event.tagId)))
    }

    private fun onTagsSearchQueryInput(query: String) {
        state { copy(tagsSearchQuery = query) }
        commands(LoadTags(query.trim()))
    }

    private fun reduceReviewInfoUi(event: ReviewInfo) = when (event) {
        is ReviewInfo.OnRatingSelect -> state { copy(reviewDetails = reviewDetails.copy(rating = event.rating)) }
        is ReviewInfo.OnCommentInput -> state { copy(reviewDetails = reviewDetails.copy(comment = event.text)) }
        is ReviewInfo.OnAdvantagesInput -> state { copy(reviewDetails = reviewDetails.copy(advantages = event.text)) }
        is ReviewInfo.OnDisadvantagesInput -> state { copy(reviewDetails = reviewDetails.copy(disadvantages = event.text)) }
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

        is OnTagCreated -> {
            state {
                copy(
                    reviewDetails = reviewDetails.copy(
                        tagsIds = reviewDetails.tagsIds + event.tagId,
                    ),
                )
            }

            onTagsSearchQueryInput("")
        }

        is OnTagDeleted -> {
            state {
                copy(
                    reviewDetails = reviewDetails.copy(
                        tagsIds = reviewDetails.tagsIds - event.tagId,
                    ),
                )
            }
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
                tagsIds = event.review.tags.map { it.id }.toSet(),
                comment = event.review.comment.orEmpty(),
                advantages = event.review.advantages.orEmpty(),
                disadvantages = event.review.disadvantages.orEmpty(),
                rating = event.review.rating,
            )

            copy(
                isReviewLoading = false,
                initialReviewDetails = initialReviewDetails,
                reviewDetails = initialReviewDetails,
            )
        }

        is ReviewLoading.Failed -> {
            state { copy(isReviewLoading = false, isReviewLoadingFailed = true) }
            effects(ShowErrorDialog)
        }
    }

    private fun reduceTagsLoading(event: TagsLoading) = when (event) {
        is TagsLoading.Started -> Unit
        is TagsLoading.Succeed -> state { copy(tagsSuggestions = event.tags) }
        is TagsLoading.Failed -> Unit
    }
}