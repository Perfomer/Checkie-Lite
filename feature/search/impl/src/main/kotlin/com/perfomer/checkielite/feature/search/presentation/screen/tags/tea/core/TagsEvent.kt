package com.perfomer.checkielite.feature.search.presentation.screen.tags.tea.core

import com.perfomer.checkielite.core.domain.entity.review.CheckieTag

internal sealed interface TagsEvent {

    data object Initialize : TagsEvent

    sealed interface TagsLoading : TagsEvent {
        data object Started : TagsLoading
        class Succeed(val tags: List<CheckieTag>) : TagsLoading
        class Failed(val error: Throwable) : TagsLoading
    }
}

internal sealed interface TagsUiEvent : TagsEvent {

    data object OnBackPress : TagsUiEvent

    data object OnDoneClick : TagsUiEvent

    class OnSearchQueryInput(val query: String) : TagsUiEvent

    data object OnSearchQueryClearClick : TagsUiEvent

    class OnTagClick(val tagId: String) : TagsUiEvent
}

internal sealed interface TagsNavigationEvent : TagsEvent