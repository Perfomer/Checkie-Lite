package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core

import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.core.entity.search.SearchFilters
import com.perfomer.checkielite.core.entity.search.SearchSorting
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.Filter.FilterType

internal sealed interface SearchEvent {

    data object Initialize : SearchEvent

    class ReviewsFiltered(val reviews: List<CheckieReview>) : SearchEvent

    sealed interface RecentSearchesLoading : SearchEvent {
        data object Started : RecentSearchesLoading
        class Succeed(val reviews: List<CheckieReview>) : RecentSearchesLoading
        class Failed(val error: Throwable) : RecentSearchesLoading
    }

    sealed interface ReviewsLoading : SearchEvent {
        data object Started : ReviewsLoading
        class Succeed(val reviews: List<CheckieReview>) : ReviewsLoading
        class Failed(val error: Throwable) : ReviewsLoading
    }

    sealed interface TagsLoading : SearchEvent {
        data object Started : TagsLoading
        class Succeed(val tags: List<CheckieTag>) : TagsLoading
        class Failed(val error: Throwable) : TagsLoading
    }
}

internal sealed interface SearchUiEvent : SearchEvent {

    data object OnBackPress : SearchUiEvent

    class OnSearchFieldInput(val text: String) : SearchUiEvent

    data object OnSearchClearClick : SearchUiEvent

    class OnFilterClick(val type: FilterType) : SearchUiEvent

    class OnReviewClick(val reviewId: String) : SearchUiEvent

    data object OnRecentSearchesClearClick : SearchUiEvent

    data object OnAllFiltersClick : SearchUiEvent
}

internal sealed interface SearchNavigationEvent : SearchEvent {

    class OnSortUpdated(val sorting: SearchSorting) : SearchNavigationEvent
    class OnTagsUpdated(val tagsIds: List<String>) : SearchNavigationEvent
    class OnFiltersUpdated(val filters: SearchFilters, val sorting: SearchSorting) : SearchNavigationEvent
}