package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.requireContent
import com.perfomer.checkielite.common.pure.state.toLoadingContentAware
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationStartAction
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand.DeleteReview
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand.LoadReview
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEffect
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEffect.ShowConfirmDeleteDialog
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEffect.ShowSyncingToast
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent.ReviewDeletion
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent.ReviewLoading
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.OpenGallery
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.OpenReviewEdit
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.OpenSearch
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsState
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnConfirmDeleteClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnDeleteClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnEditClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnEmptyImageClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnEmptyPriceClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnEmptyReviewTextClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnPictureClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnPictureSelect
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnRecommendationClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnStart
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnTagClick

internal class ReviewDetailsReducer : DslReducer<ReviewDetailsCommand, ReviewDetailsEffect, ReviewDetailsEvent, ReviewDetailsState>() {

    override fun reduce(event: ReviewDetailsEvent) = when (event) {
        is ReviewDetailsUiEvent -> reduceUi(event)
        is ReviewLoading -> reduceReviewLoading(event)
        is ReviewDeletion -> reduceReviewDeletion(event)
    }

    private fun reduceUi(event: ReviewDetailsUiEvent) = when (event) {
        is OnStart -> commands(LoadReview(state.reviewId))
        is OnBackPress -> commands(Exit)
        is OnDeleteClick -> effects(ShowConfirmDeleteDialog)
        is OnConfirmDeleteClick -> commands(DeleteReview(state.reviewId))
        is OnEmptyImageClick -> reduceOnEditClick(startAction = ReviewCreationStartAction.ADD_PICTURES)
        is OnEmptyPriceClick -> reduceOnEditClick(startAction = ReviewCreationStartAction.SET_PRICE)
        is OnEmptyReviewTextClick -> reduceOnEditClick(startAction = ReviewCreationStartAction.ADD_REVIEW_COMMENT)
        is OnEditClick -> reduceOnEditClick(startAction = ReviewCreationStartAction.NONE)
        is OnTagClick -> commands(OpenSearch(tagId = event.tagId))
        is OnRecommendationClick -> commands(OpenReviewDetails(event.recommendedReviewId))
        is OnPictureSelect -> state { copy(currentPicturePosition = event.position) }
        is OnPictureClick -> commands(
            OpenGallery(
                picturesUri = state.review.requireContent().review.pictures.map { it.uri },
                currentPicturePosition = state.currentPicturePosition,
            )
        )
    }

    private fun reduceReviewLoading(event: ReviewLoading) = when (event) {
        is ReviewLoading.Started -> state { copy(review = state.review.toLoadingContentAware()) }
        is ReviewLoading.Succeed -> state { copy(review = Lce.Content(event.details)) }
        is ReviewLoading.Failed -> state { copy(review = Lce.Error()) }
    }

    private fun reduceReviewDeletion(event: ReviewDeletion) = when (event) {
        is ReviewDeletion.Started -> Unit
        is ReviewDeletion.Succeed -> commands(Exit)
        is ReviewDeletion.Failed -> state { copy(review = Lce.Error()) }
    }

    private fun reduceOnEditClick(startAction: ReviewCreationStartAction) {
        if (state.review.requireContent().review.isSyncing) {
            effects(ShowSyncingToast)
        } else {
            commands(OpenReviewEdit(reviewId = state.reviewId, startAction = startAction))
        }
    }
}