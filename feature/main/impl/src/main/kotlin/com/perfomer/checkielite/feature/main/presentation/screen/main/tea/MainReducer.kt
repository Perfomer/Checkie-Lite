package com.perfomer.checkielite.feature.main.presentation.screen.main.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.requireContent
import com.perfomer.checkielite.common.pure.state.toLoadingContentAware
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand.LoadReviews
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainCommand.LoadTags
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEffect
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent.ReviewsLoading
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainEvent.TagsLoading
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
import com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core.MainUiEvent.OnTagClick

internal class MainReducer : DslReducer<MainCommand, MainEffect, MainEvent, MainState>() {

    override fun reduce(event: MainEvent) = when (event) {
        is MainUiEvent -> reduceUi(event)
        is MainNavigationEvent -> reduceNavigation(event)
        is ReviewsLoading -> reduceReviewsLoading(event)
        is TagsLoading -> reduceTagsLoading(event)
    }

    private fun reduceUi(event: MainUiEvent) = when (event) {
        is OnStart -> commands(LoadReviews, LoadTags)
        is OnFabClick -> commands(OpenReviewCreation)
        is OnReviewClick -> commands(OpenReviewDetails(event.id))
        is OnTagClick -> commands(OpenSearch(state.tags.requireContent().first { it.id == event.id }))
        is OnSearchClick -> commands(OpenSearch())
    }

    private fun reduceNavigation(event: MainNavigationEvent) = when (event) {
        is ReviewCreated -> Unit
    }

    private fun reduceReviewsLoading(event: ReviewsLoading) = when (event) {
        is ReviewsLoading.Started -> state { copy(reviews = reviews.toLoadingContentAware()) }
        is ReviewsLoading.Succeed -> state { copy(reviews = Lce.Content(event.reviews)) }
        is ReviewsLoading.Failed -> state { copy(reviews = Lce.Error(event.error)) }
    }

    private fun reduceTagsLoading(event: TagsLoading) = when (event) {
        is TagsLoading.Started -> state { copy(tags = tags.toLoadingContentAware()) }
        is TagsLoading.Succeed -> state { copy(tags = Lce.Content(event.tags)) }
        is TagsLoading.Failed -> state { copy(tags = Lce.Error(event.error)) }
    }
}