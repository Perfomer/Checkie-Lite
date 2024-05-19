package com.perfomer.checkielite.feature.search.presentation.screen.search.tea

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.toLoadingContentAware
import com.perfomer.checkielite.common.tea.dsl.DslReducer
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
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenSort
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenTags
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent.OnSortUpdated
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent.OnTagsUpdated
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchState
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnBackPress
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnClearAllFiltersClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnFilterClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnRecentSearchesClearClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnReviewClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnSearchClearClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnSearchFieldInput
import com.perfomer.checkielite.feature.search.presentation.screen.search.ui.state.Filter.FilterType

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
        is OnFilterClick -> reduceOnFilterClick(event)
        is OnReviewClick -> commands(
            RememberRecentSearch(event.reviewId),
            OpenReviewDetails(event.reviewId),
        )

        is OnSearchClearClick -> updateSearchConditions(query = "")
        is OnSearchFieldInput -> updateSearchConditions(query = event.text)
        is OnRecentSearchesClearClick -> {
            state { copy(recentSearches = Lce.Content(emptyList())) }
            commands(ClearRecentSearches)
        }

        is OnClearAllFiltersClick -> updateSearchConditions(filters = SearchFilters(), sorting = SearchSorting.default)
    }

    private fun reduceOnFilterClick(event: OnFilterClick) = when (event.type) {
        FilterType.TAGS -> commands(OpenTags(state.searchFilters.tags))
        FilterType.RATING -> Unit
        FilterType.SORT -> commands(OpenSort(state.searchSorting))
    }

    private fun reduceNavigation(event: SearchNavigationEvent) = when (event) {
        is OnTagsUpdated -> updateSearchConditions(filters = state.searchFilters.copy(tags = event.tags))
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
                query = state.searchQuery.trim(),
                filters = state.searchFilters,
                sorting = state.searchSorting,
            )
        )
    }

}