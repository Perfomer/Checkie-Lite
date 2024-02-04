package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core

import com.perfomer.checkielite.core.entity.CheckieReview

internal sealed interface MainEvent {

    data object Initialize : MainEvent

    sealed interface ReviewsLoading : MainEvent {

        class Succeed(val reviews: List<CheckieReview>) : ReviewsLoading
    }
}

internal sealed interface MainUiEvent : MainEvent {

    class OnReviewClick(val id: String) : MainUiEvent

    class OnSearchQueryInput(val query: String) : MainUiEvent

    data object OnSearchQueryClearClick : MainUiEvent

    data object OnFabClick : MainUiEvent
}