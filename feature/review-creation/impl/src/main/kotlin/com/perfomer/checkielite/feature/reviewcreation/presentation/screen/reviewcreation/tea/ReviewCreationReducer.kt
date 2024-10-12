package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea

import com.perfomer.checkielite.common.pure.util.next
import com.perfomer.checkielite.common.pure.util.previous
import com.perfomer.checkielite.common.pure.util.swap
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.core.entity.CheckiePicture
import com.perfomer.checkielite.core.entity.PictureSource
import com.perfomer.checkielite.core.entity.price.CheckiePrice
import com.perfomer.checkielite.core.entity.sort.TagSortingStrategy
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationMode
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationStartAction
import com.perfomer.checkielite.feature.reviewcreation.navigation.ReviewCreationResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.entity.TagCreationMode
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.CreateReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.LoadLatestCurrency
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.LoadLatestTagSortStrategy
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.LoadReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.LoadTags
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.RememberTagSortStrategy
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.SearchBrands
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.UpdateReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.WarmUpCurrencies
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.CloseKeyboard
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.FocusCommentField
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.FocusPriceField
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.ShowConfirmExitDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect.ShowErrorDialog
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.BrandsSearchComplete
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.LatestCurrencyLoading
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.LatestTagSortStrategyLoading
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.ReviewLoading
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.ReviewSaving
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.TagsLoading
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.ExitWithResult
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenCurrencySelector
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenGallery
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenPhotoPicker
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.OpenTagCreation
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnCurrencySelected
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnPhotosPick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnTagCreated
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnTagDeleted
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationEvent.OnTagSortSelected
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnConfirmExitClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnPrimaryButtonClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ProductInfo
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.ReviewInfo
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.Tags
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewDetails
import com.perfomer.checkielite.feature.reviewcreation.presentation.util.LocalCheckieCurrency
import com.perfomer.checkielite.feature.reviewcreation.presentation.util.toBigDecimalSafe
import kotlinx.collections.immutable.toPersistentList

internal class ReviewCreationReducer : DslReducer<ReviewCreationCommand, ReviewCreationEffect, ReviewCreationEvent, ReviewCreationState>() {

    override fun reduce(event: ReviewCreationEvent) = when (event) {
        is Initialize -> reduceInitialize()

        is ReviewCreationUiEvent -> reduceUi(event)
        is ReviewCreationNavigationEvent -> reduceNavigation(event)

        is ReviewLoading -> reduceReviewLoading(event)
        is ReviewSaving -> reduceReviewsCreation(event)
        is TagsLoading -> reduceTagsLoading(event)
        is LatestCurrencyLoading -> reduceLatestCurrencyLoading(event)
        is LatestTagSortStrategyLoading -> reduceLatestTagSortStrategyLoading(event)

        is BrandsSearchComplete -> state { copy(suggestedBrands = event.brands) }
    }

    private fun reduceInitialize() {
        val modificationMode = state.mode as? ReviewCreationMode.Modification
        if (modificationMode != null) commands(LoadReview(modificationMode.reviewId))

        commands(
            LoadTags(sort = state.tagSorting),
            WarmUpCurrencies,
            LoadLatestCurrency,
            LoadLatestTagSortStrategy,
        )

        when (state.mode.startAction) {
            ReviewCreationStartAction.NONE -> Unit
            ReviewCreationStartAction.SET_PRICE -> effects(FocusPriceField)
            ReviewCreationStartAction.ADD_PICTURES -> commands(OpenPhotoPicker)
            ReviewCreationStartAction.ADD_REVIEW_COMMENT -> effects(FocusCommentField)
        }
    }

    private fun reduceUi(event: ReviewCreationUiEvent) = when (event) {
        is ProductInfo -> reduceProductInfoUi(event)
        is Tags -> reduceTagsUi(event)
        is ReviewInfo -> reduceReviewInfoUi(event)
        is OnPrimaryButtonClick -> {
            val next = state.currentPage.next()

            if (next != null) {
                val isProductNameValid = state.reviewDetails.productName.isNotBlank()
                if (isProductNameValid) {
                    state { copy(currentPage = next) }
                    effects(CloseKeyboard)
                }
                state { copy(isProductNameValid = isProductNameValid) }
            } else {
                when (val mode = state.mode) {
                    is ReviewCreationMode.Creation -> commands(
                        CreateReview(
                            productName = state.reviewDetails.productName.trim(),
                            productBrand = state.reviewDetails.productBrand.trim(),
                            price = state.reviewDetails.price,
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
                            price = state.reviewDetails.price,
                            comment = state.reviewDetails.comment.trim(),
                            advantages = state.reviewDetails.advantages.trim(),
                            disadvantages = state.reviewDetails.disadvantages.trim(),
                            rating = state.reviewDetails.rating,
                            pictures = state.reviewDetails.pictures,
                            tagsIds = state.reviewDetails.tagsIds,
                        )
                    )
                }
                effects(CloseKeyboard)
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
        is ProductInfo.OnPriceTextInput -> reduceOnPriceTextInput(event)
        is ProductInfo.OnPriceCurrencyClick -> {
            effects(CloseKeyboard)
            commands(OpenCurrencySelector(state.currentPriceCurrency))
        }
        is ProductInfo.OnAddPictureClick -> {
            effects(CloseKeyboard)
            commands(OpenPhotoPicker)
        }
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

    @Suppress("IfThenToElvis")
    private fun reduceOnPriceTextInput(event: ProductInfo.OnPriceTextInput) {
        val input = event.text
        val priceInputValue = input.toBigDecimalSafe()
        val currentPrice = state.reviewDetails.price

        val price = if (priceInputValue == null) {
            null
        } else {
            if (currentPrice == null) {
                CheckiePrice(value = priceInputValue, currency = state.currentPriceCurrency)
            } else {
                currentPrice.copy(value = priceInputValue)
            }
        }

        state {
            copy(
                currentPriceFieldValue = input,
                reviewDetails = reviewDetails.copy(price = price),
            )
        }
    }

    private fun reduceTagsUi(event: Tags) = when (event) {
        is Tags.OnCreateTagClick -> {
            commands(OpenTagCreation(TagCreationMode.Creation(state.tagsSearchQuery.trim())))
            onTagsSearchQueryUpdate("")
        }
        is Tags.OnSearchQueryClearClick -> onTagsSearchQueryUpdate("")
        is Tags.OnSearchQueryInput -> onTagsSearchQueryUpdate(event.query)
        is Tags.OnTagSortClick -> commands(ReviewCreationNavigationCommand.OpenTagSort(state.tagSorting))
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
            onTagsSearchQueryUpdate("")
        }
        is Tags.OnTagLongClick -> {
            effects(CloseKeyboard)
            commands(OpenTagCreation(TagCreationMode.Modification(event.tagId)))
            onTagsSearchQueryUpdate("")
        }
    }

    private fun onTagsSearchQueryUpdate(
        query: String = state.tagsSearchQuery,
        sort: TagSortingStrategy = state.tagSorting,
    ) {
        state { copy(tagsSearchQuery = query, tagSorting = sort) }
        commands(LoadTags(query.trim(), sort))
    }

    private fun reduceReviewInfoUi(event: ReviewInfo) = when (event) {
        is ReviewInfo.OnRatingSelect -> state { copy(reviewDetails = reviewDetails.copy(rating = event.rating)) }
        is ReviewInfo.OnCommentInput -> state { copy(reviewDetails = reviewDetails.copy(comment = event.text)) }
        is ReviewInfo.OnAdvantagesInput -> state { copy(reviewDetails = reviewDetails.copy(advantages = event.text)) }
        is ReviewInfo.OnDisadvantagesInput -> state { copy(reviewDetails = reviewDetails.copy(disadvantages = event.text)) }
    }

    private fun reduceNavigation(event: ReviewCreationNavigationEvent) = when (event) {
        is OnPhotosPick -> state {
            val addedPictures = event.uris.map { uri ->
                CheckiePicture(uri = uri, source = PictureSource.GALLERY)
            }

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

            onTagsSearchQueryUpdate("")
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
        is OnTagSortSelected -> {
            onTagsSearchQueryUpdate(sort = event.strategy)
            commands(RememberTagSortStrategy(event.strategy))
        }
        is OnCurrencySelected -> {
            val selectedCurrency = event.currency
            val currentPrice = state.reviewDetails.price

            if (currentPrice != null) {
                state { copy(reviewDetails = reviewDetails.copy(price = currentPrice.copy(currency = selectedCurrency))) }
            }

            state { copy(currentPriceCurrency = selectedCurrency) }
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
                price = event.review.price,
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
                currentPriceFieldValue = initialReviewDetails.price?.value?.toString().orEmpty(),
                currentPriceCurrency = initialReviewDetails.price?.currency ?: currentPriceCurrency,
            )
        }

        is ReviewLoading.Failed -> {
            state { copy(isReviewLoading = false, isReviewLoadingFailed = true) }
            effects(ShowErrorDialog)
        }
    }

    private fun reduceTagsLoading(event: TagsLoading) = when (event) {
        is TagsLoading.Started -> Unit
        is TagsLoading.Succeed -> {
            state { copy(tagsSuggestions = event.tags) }
            onTagsSearchQueryUpdate()
        }
        is TagsLoading.Failed -> Unit
    }

    private fun reduceLatestCurrencyLoading(event: LatestCurrencyLoading) = when (event) {
        is LatestCurrencyLoading.Started -> Unit
        is LatestCurrencyLoading.Succeed -> {
            val defaultCurrency = event.currency ?: LocalCheckieCurrency.getLocalCurrency()
            state { copy(currentPriceCurrency = initialReviewDetails.price?.currency ?: defaultCurrency) }
        }
        is LatestCurrencyLoading.Failed -> Unit
    }


    private fun reduceLatestTagSortStrategyLoading(event: LatestTagSortStrategyLoading) = when (event) {
        is LatestTagSortStrategyLoading.Started -> Unit
        is LatestTagSortStrategyLoading.Succeed -> {
            val strategy = event.strategy ?: TagSortingStrategy.USAGE_COUNT
            onTagsSearchQueryUpdate(sort = strategy)
        }
        is LatestTagSortStrategyLoading.Failed -> Unit
    }
}