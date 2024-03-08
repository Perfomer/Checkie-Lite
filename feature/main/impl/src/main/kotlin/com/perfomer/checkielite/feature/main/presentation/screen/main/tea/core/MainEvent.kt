package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core

import com.perfomer.checkielite.core.entity.CheckieReview

internal sealed interface MainEvent {

    sealed interface ReviewsLoading : MainEvent {

        data object Started : ReviewsLoading

        class Succeed(
            val reviews: List<CheckieReview>,
            val searchQuery: String,
        ) : ReviewsLoading

        class Failed(val error: Throwable) : ReviewsLoading
    }
}

internal sealed interface MainUiEvent : MainEvent {

    data object OnStart : MainUiEvent

    class OnReviewClick(val id: String) : MainUiEvent

    class OnSearchQueryInput(val query: String) : MainUiEvent

    data object OnSearchQueryClearClick : MainUiEvent

    data object OnFabClick : MainUiEvent
}

internal sealed interface MainNavigationEvent : MainEvent {

    data object ReviewCreated : MainNavigationEvent
}