package com.perfomer.checkielite.feature.search.presentation.screen.search.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.toLoadingContentAware
import com.perfomer.checkielite.common.pure.util.toArrayList
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.core.entity.search.RatingRange
import com.perfomer.checkielite.core.entity.search.SearchFilters
import com.perfomer.checkielite.core.entity.search.SearchSorting
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.ClearRecentSearches
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.LoadRecentSearches
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.RememberRecentSearch
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEffect
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.Initialize
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.RecentSearchesLoading
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.Searching
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.Exit
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenFilters
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenSort
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent.OnFiltersUpdated
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent.OnSortUpdated
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchState
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnBackPress
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnFilterClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnRatingRangeFilterClearClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnRecentSearchesClearClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnReviewClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnSearchClearClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnSearchFieldInput
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnSortClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnTagFilterClearClick

internal class SearchReducer : DslReducer<SearchCommand, SearchEffect, SearchEvent, SearchState>() {

    override fun reduce(event: SearchEvent) = when (event) {
        is Initialize -> reduceInitialize()
        is SearchUiEvent -> reduceUi(event)
        is SearchNavigationEvent -> reduceNavigation(event)
        is RecentSearchesLoading -> reduceRecentSearchesLoading(event)
        is Searching -> reduceSearching(event)
    }

    private fun reduceInitialize() {
        if (state.hasSearchConditions) {
            updateSearchConditions()
        }

        commands(LoadRecentSearches)
    }

    private fun reduceUi(event: SearchUiEvent) = when (event) {
        is OnBackPress -> commands(Exit)
        is OnSortClick -> commands(OpenSort(state.searchSorting))
        is OnFilterClick -> commands(OpenFilters(state.searchFilters))
        is OnReviewClick -> commands(RememberRecentSearch(event.reviewId), OpenReviewDetails(event.reviewId))
        is OnRatingRangeFilterClearClick -> {
            updateSearchConditions(filters = state.searchFilters.copy(ratingRange = RatingRange.default))
        }

        is OnTagFilterClearClick -> {
            updateSearchConditions(
                filters = state.searchFilters.copy(
                    tags = state.searchFilters.tags.filter { it.id == event.tagId }.toArrayList(),
                )
            )
        }

        is OnSearchClearClick -> updateSearchConditions(query = "")
        is OnSearchFieldInput -> updateSearchConditions(query = event.text)
        is OnRecentSearchesClearClick -> {
            state { copy(recentSearches = Lce.Content(emptyList())) }
            commands(ClearRecentSearches)
        }
    }

    private fun reduceNavigation(event: SearchNavigationEvent) = when (event) {
        is OnFiltersUpdated -> updateSearchConditions(filters = event.filters)
        is OnSortUpdated -> updateSearchConditions(sorting = event.sorting)
    }

    private fun reduceRecentSearchesLoading(event: RecentSearchesLoading) = when (event) {
        is RecentSearchesLoading.Started -> state { copy(recentSearches = Lce.Loading()) }
        is RecentSearchesLoading.Succeed -> state { copy(recentSearches = Lce.Content(event.reviews)) }
        is RecentSearchesLoading.Failed -> state { copy(recentSearches = Lce.Error(event.error)) }
    }

    private fun reduceSearching(event: Searching) = when (event) {
        is Searching.Started -> state { copy(searchedReviews = state.recentSearches.toLoadingContentAware()) }
        is Searching.Succeed -> state { copy(searchedReviews = Lce.Content(event.reviews)) }
        is Searching.Failed -> state { copy(recentSearches = Lce.Error(event.error)) }
    }

    private fun updateSearchConditions(
        query: String = state.searchQuery,
        filters: SearchFilters = state.searchFilters,
        sorting: SearchSorting = state.searchSorting,
    ) {
        state {
            copy(
                searchFilters = filters,
                searchQuery = query,
                searchSorting = sorting,
            )
        }

        commands(
            SearchCommand.SearchReviews(
                query = state.searchQuery,
                filters = state.searchFilters,
                sorting = state.searchSorting,
            )
        )
    }

}