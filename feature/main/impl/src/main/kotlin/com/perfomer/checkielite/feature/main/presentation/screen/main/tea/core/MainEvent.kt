package com.perfomer.checkielite.feature.main.presentation.screen.main.tea.core

import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.CheckieTag

internal sealed interface MainEvent {

    data object Initialize : MainEvent

    sealed interface ReviewsLoading : MainEvent {
        data object Started : ReviewsLoading
        class Succeed(val reviews: List<CheckieReview>) : ReviewsLoading
        class Failed(val error: Throwable) : ReviewsLoading
    }

    sealed interface TagsLoading : MainEvent {
        data object Started : TagsLoading
        class Succeed(val tags: List<CheckieTag>) : TagsLoading
        class Failed(val error: Throwable) : TagsLoading
    }
}

internal sealed interface MainUiEvent : MainEvent {

    class OnReviewClick(val id: String) : MainUiEvent

    class OnTagClick(val id: String) : MainUiEvent

    data object OnSearchClick : MainUiEvent

    data object OnSettingsClick : MainUiEvent

    data object OnFabClick : MainUiEvent
}

internal sealed interface MainNavigationEvent : MainEvent {

    data object ReviewCreated : MainNavigationEvent
}