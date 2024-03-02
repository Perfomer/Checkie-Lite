package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand.DeleteReview
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand.LoadReview
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEffect
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEffect.ShowConfirmDeleteDialog
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent.Initialize
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent.ReviewDeletion
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent.ReviewLoading
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.OpenReviewEdit
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsState
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent

internal class ReviewDetailsReducer : DslReducer<ReviewDetailsCommand, ReviewDetailsEffect, ReviewDetailsEvent, ReviewDetailsState>() {

    override fun reduce(event: ReviewDetailsEvent) = when (event) {
        is Initialize -> commands(LoadReview(state.reviewId))
        is ReviewDetailsUiEvent -> reduceUi(event)
        is ReviewLoading -> reduceReviewLoading(event)
        is ReviewDeletion.Succeed -> reduceReviewDeletion(event)
    }

    private fun reduceUi(event: ReviewDetailsUiEvent) = when (event) {
        is ReviewDetailsUiEvent.OnBackPress -> commands(Exit)
        is ReviewDetailsUiEvent.OnDeleteClick -> effects(ShowConfirmDeleteDialog)
        is ReviewDetailsUiEvent.OnConfirmDeleteClick -> commands(DeleteReview(state.reviewId))
        is ReviewDetailsUiEvent.OnEmptyImageClick -> reduceOnEditClick(initialPage = ReviewCreationPage.PRODUCT_INFO)
        is ReviewDetailsUiEvent.OnEmptyReviewTextClick -> reduceOnEditClick(initialPage = ReviewCreationPage.REVIEW_INFO)
        is ReviewDetailsUiEvent.OnEditClick -> reduceOnEditClick(initialPage = ReviewCreationPage.PRODUCT_INFO)
        is ReviewDetailsUiEvent.OnPictureSelect -> state { copy(currentPicturePosition = event.position) }
    }

    private fun reduceReviewLoading(event: ReviewLoading) = when (event) {
        is ReviewLoading.Succeed -> state { copy(review = Lce.Content(event.review)) }
    }

    private fun reduceReviewDeletion(event: ReviewDeletion) = when (event) {
        is ReviewDeletion.Succeed -> commands(Exit)
    }

    private fun reduceOnEditClick(initialPage: ReviewCreationPage) {
        commands(OpenReviewEdit(reviewId = state.reviewId, initialPage = initialPage))
    }
}