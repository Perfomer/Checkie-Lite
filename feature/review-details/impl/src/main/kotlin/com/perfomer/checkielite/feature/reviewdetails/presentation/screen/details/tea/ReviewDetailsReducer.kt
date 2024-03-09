package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.requireContent
import com.perfomer.checkielite.common.pure.state.toLoadingContentAware
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.reviewcreation.entity.ReviewCreationPage
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
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.OpenReviewEdit
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsState
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnConfirmDeleteClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnDeleteClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnEditClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnEmptyImageClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnEmptyReviewTextClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnPictureClick
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnPictureSelect
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent.OnStart

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
        is OnEmptyImageClick -> reduceOnEditClick(initialPage = ReviewCreationPage.PRODUCT_INFO)
        is OnEmptyReviewTextClick -> reduceOnEditClick(initialPage = ReviewCreationPage.REVIEW_INFO)
        is OnEditClick -> reduceOnEditClick(initialPage = ReviewCreationPage.PRODUCT_INFO)
        is OnPictureSelect -> state { copy(currentPicturePosition = event.position) }
        is OnPictureClick -> commands(
            OpenGallery(
                picturesUri = state.review.requireContent().pictures.map { it.uri },
                currentPicturePosition = state.currentPicturePosition,
            )
        )
    }

    private fun reduceReviewLoading(event: ReviewLoading) = when (event) {
        is ReviewLoading.Started -> state { copy(review = state.review.toLoadingContentAware()) }
        is ReviewLoading.Succeed -> state { copy(review = Lce.Content(event.review)) }
        is ReviewLoading.Failed -> state { copy(review = Lce.Error()) }
    }

    private fun reduceReviewDeletion(event: ReviewDeletion) = when (event) {
        is ReviewDeletion.Started -> Unit
        is ReviewDeletion.Succeed -> commands(Exit)
        is ReviewDeletion.Failed -> state { copy(review = Lce.Error()) }
    }

    private fun reduceOnEditClick(initialPage: ReviewCreationPage) {
        if (state.review.requireContent().isSyncing) {
            effects(ShowSyncingToast)
        } else {
            commands(OpenReviewEdit(reviewId = state.reviewId, initialPage = initialPage))
        }
    }
}