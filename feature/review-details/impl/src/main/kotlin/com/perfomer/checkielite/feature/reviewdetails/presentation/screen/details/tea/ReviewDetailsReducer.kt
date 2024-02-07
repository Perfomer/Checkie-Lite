package com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsCommand.LoadReview
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEffect
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent.Initialize
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsEvent.ReviewLoading
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsNavigationCommand.OpenReviewEdit
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsState
import com.perfomer.checkielite.feature.reviewdetails.presentation.screen.details.tea.core.ReviewDetailsUiEvent

internal class ReviewDetailsReducer : DslReducer<ReviewDetailsCommand, ReviewDetailsEffect, ReviewDetailsEvent, ReviewDetailsState>() {

    override fun reduce(event: ReviewDetailsEvent) = when (event) {
        is Initialize -> commands(LoadReview(state.reviewId))
        is ReviewDetailsUiEvent -> reduceUi(event)
        is ReviewLoading -> reduceReviewsLoading(event)
    }

    private fun reduceUi(event: ReviewDetailsUiEvent) = when (event) {
        is ReviewDetailsUiEvent.OnBackPress -> commands(Exit)
        is ReviewDetailsUiEvent.OnDeleteClick -> Unit
        is ReviewDetailsUiEvent.OnEditClick -> commands(OpenReviewEdit(state.reviewId))
        is ReviewDetailsUiEvent.OnPictureSelect -> state { copy(currentPicturePosition = event.position) }
    }

    private fun reduceReviewsLoading(event: ReviewLoading) = when (event) {
        is ReviewLoading.Succeed -> state { copy(review = Lce.Content(event.review)) }
    }
}