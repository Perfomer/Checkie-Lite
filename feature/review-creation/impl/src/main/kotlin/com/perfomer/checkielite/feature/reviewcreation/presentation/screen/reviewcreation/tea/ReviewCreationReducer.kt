package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea

import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.CreateReview
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEffect
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.Initialize
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.ReviewCreation
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationNavigationCommand.Exit
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationState
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnBackPress
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationUiEvent.OnPrimaryButtonClick
import com.perfomer.checkielite.feature.reviewcreation.presentation.util.next
import com.perfomer.checkielite.feature.reviewcreation.presentation.util.previous

internal class ReviewCreationReducer : DslReducer<ReviewCreationCommand, ReviewCreationEffect, ReviewCreationEvent, ReviewCreationState>() {

    override fun reduce(event: ReviewCreationEvent) = when (event) {
        is Initialize -> Unit

        is ReviewCreationUiEvent -> reduceUi(event)
        is ReviewCreation -> reduceReviewsCreation(event)
    }

    private fun reduceUi(event: ReviewCreationUiEvent) = when (event) {
        is OnPrimaryButtonClick -> {
            val next = state.currentPage.next()

            if (next != null) {
                state { copy(currentPage = next) }
            } else {
                // todo commands(CreateReview())
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

    private fun reduceReviewsCreation(event: ReviewCreation) = when (event) {
        is ReviewCreation.Succeed -> Unit
    }
}