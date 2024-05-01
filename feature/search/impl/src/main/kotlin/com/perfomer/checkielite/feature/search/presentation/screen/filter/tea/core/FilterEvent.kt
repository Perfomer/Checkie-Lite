package com.perfomer.checkielite.feature.search.presentation.screen.filter.tea.core

import com.perfomer.checkielite.core.entity.CheckieTag

internal sealed interface FilterEvent {

    data object Initialize : FilterEvent

    sealed interface TagsLoading : FilterEvent {
        data object Started : TagsLoading
        class Succeed(val tags: List<CheckieTag>) : TagsLoading
        class Failed(val error: Throwable) : TagsLoading
    }
}

internal sealed interface FilterUiEvent : FilterEvent {

    data object OnBackPress : FilterUiEvent

    data object OnDoneClick : FilterUiEvent

    data object OnResetClick : FilterUiEvent

    data object OnViewAllTagsClick : FilterUiEvent

    class OnTagClick(val tagId: String) : FilterUiEvent

    class OnMinRatingSelected(val rating: Int) : FilterUiEvent

    class OnMaxRatingSelected(val rating: Int) : FilterUiEvent
}

internal sealed interface FilterNavigationEvent : FilterEvent {

    class OnTagsSelected(val tags: List<CheckieTag>) : FilterNavigationEvent
}