package com.perfomer.checkielite.feature.main.presentation.screen.main.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.toLoadingContentAware
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand.LoadReviews
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEffect
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent.ReviewsLoading
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand.OpenReviewCreation
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationCommand.OpenSearch
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainNavigationEvent.ReviewCreated
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainState
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnFabClick
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnReviewClick
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnSearchClick
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnStart

internal class MainReducer : DslReducer<MainCommand, MainEffect, MainEvent, MainState>() {

    override fun reduce(event: MainEvent) = when (event) {
        is MainUiEvent -> reduceUi(event)
        is MainNavigationEvent -> reduceNavigation(event)
        is ReviewsLoading -> reduceReviewsLoading(event)
    }

    private fun reduceUi(event: MainUiEvent) = when (event) {
        is OnStart -> commands(LoadReviews(state.searchQuery))
        is OnFabClick -> commands(OpenReviewCreation)
        is OnReviewClick -> commands(OpenReviewDetails(event.id))
        is OnSearchClick -> commands(OpenSearch())
    }

    private fun reduceNavigation(event: MainNavigationEvent) = when (event) {
        is ReviewCreated -> commands(LoadReviews())
    }

    private fun reduceReviewsLoading(event: ReviewsLoading) = when (event) {
        is ReviewsLoading.Started -> state { copy(reviews = state.reviews.toLoadingContentAware()) }
        is ReviewsLoading.Succeed -> state {
            if (event.searchQuery.isEmpty()) copy(reviews = Lce.Content(event.reviews), searchedReviews = Lce.initial())
            else copy(searchedReviews = Lce.Content(event.reviews))
        }
        is ReviewsLoading.Failed -> state { copy(reviews = Lce.Error(event.error)) }
    }
}