package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core

import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.core.entity.search.SearchSorting
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.Filter.FilterType

internal sealed interface SearchEvent {

    data object Initialize : SearchEvent

    sealed interface RecentSearchesLoading : SearchEvent {
        data object Started : RecentSearchesLoading
        class Succeed(val reviews: List<CheckieReview>) : RecentSearchesLoading
        class Failed(val error: Throwable) : RecentSearchesLoading
    }

    sealed interface Searching : SearchEvent {
        data object Started : Searching
        class Succeed(val reviews: List<CheckieReview>) : Searching
        class Failed(val error: Throwable) : Searching
    }
}

internal sealed interface SearchUiEvent : SearchEvent {

    data object OnBackPress : SearchUiEvent

    class OnSearchFieldInput(val text: String) : SearchUiEvent

    data object OnSearchClearClick : SearchUiEvent

    class OnFilterClick(val type: FilterType) : SearchUiEvent

    class OnReviewClick(val reviewId: String) : SearchUiEvent

    data object OnRecentSearchesClearClick : SearchUiEvent

    data object OnClearAllFiltersClick : SearchUiEvent
}

internal sealed interface SearchNavigationEvent : SearchEvent {

    class OnSortUpdated(val sorting: SearchSorting) : SearchNavigationEvent
    class OnTagsUpdated(val tags: ArrayList<CheckieTag>) : SearchNavigationEvent
}