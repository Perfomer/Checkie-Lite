package com.perfomer.checkielite.feature.main.presentation.screen.main.tea

import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand.LoadReviews
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEffect
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent.*
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand.OpenReviewCreation
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainState
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.*
import com.perfomer.checkielite.common.tea.dsl.DslReducer

internal class MainReducer : DslReducer<MainCommand, MainEffect, MainEvent, MainState>() {

    override fun reduce(event: MainEvent) = when (event) {
        is Initialize -> {
            commands(LoadReviews())
        }

        is MainUiEvent -> reduceUi(event)
        is ReviewsLoading -> reduceReviewsLoading(event)
    }

    private fun reduceUi(event: MainUiEvent) = when (event) {
        is OnFabClick -> commands(OpenReviewCreation)
        is OnReviewClick -> commands(OpenReviewDetails(event.id))
        is OnSearchQueryInput -> {
            state { copy(searchQuery = event.query) }
            commands(LoadReviews(event.query))
        }
        is OnSearchQueryClearClick -> {
            state { copy(searchQuery = "") }
            commands(LoadReviews())
        }
    }

    private fun reduceReviewsLoading(event: ReviewsLoading) = when (event) {
        is ReviewsLoading.Succeed -> state { copy(reviews = event.reviews) }
    }
}