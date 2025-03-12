package com.perfomer.checkielite.feature.search.presentation.screen.search.tea

import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastFilter
import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.content
import com.perfomer.checkielite.common.pure.state.toLoading
import com.perfomer.checkielite.common.tea.dsl.DslReducer
import com.perfomer.checkielite.core.domain.entity.search.SearchFilters
import com.perfomer.checkielite.core.domain.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.ClearRecentSearches
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.FilterReviews
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.LoadRecentSearches
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.LoadReviews
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.LoadTags
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchCommand.RememberRecentSearch
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEffect
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEffect.ShowKeyboard
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.Initialize
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.RecentSearchesLoading
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.ReviewsFiltered
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.ReviewsLoading
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchEvent.TagsLoading
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.Exit
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenReviewDetails
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenSort
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationCommand.OpenTags
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent.OnFiltersUpdated
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent.OnSortUpdated
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchNavigationEvent.OnTagsUpdated
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchState
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnAllFiltersClick
import com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core.SearchUiEvent.OnBackPress
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
        is TagsLoading -> reduceTagsLoading(event)
        is ReviewsLoading -> reduceReviewsLoading(event)
        is ReviewsFiltered -> reduceReviewsFiltered(event)
    }

    private fun reduceInitialize() {
        if (state.hasSearchConditions) {
            updateSearchConditions()
        } else {
            effects(ShowKeyboard)
        }

        commands(LoadRecentSearches, LoadReviews, LoadTags)
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
        is OnAllFiltersClick -> updateSearchConditions(filters = SearchFilters(), sorting = ReviewsSortingStrategy.RELEVANCE)
    }

    private fun reduceOnFilterClick(event: OnFilterClick) = when (event.type) {
        FilterType.TAGS -> commands(OpenTags(state.searchFilters.tagsIds))
        FilterType.RATING -> Unit
        FilterType.SORT -> commands(OpenSort(state.sortingStrategy))
    }

    private fun reduceNavigation(event: SearchNavigationEvent) = when (event) {
        is OnTagsUpdated -> updateSearchConditions(filters = state.searchFilters.copy(tagsIds = event.tagsIds))
        is OnSortUpdated -> updateSearchConditions(sorting = event.sorting)
        is OnFiltersUpdated -> updateSearchConditions(filters = event.filters, sorting = event.sorting)
    }

    private fun reduceTagsLoading(event: TagsLoading) = when (event) {
        is TagsLoading.Started -> state { copy(allTags = allTags.toLoading()) }
        is TagsLoading.Failed -> state { copy(allTags = Lce.Error(event.error)) }
        is TagsLoading.Succeed -> {
            state { copy(allTags = Lce.Content(event.tags)) }

            updateSearchConditions(
                filters = state.searchFilters.copy(
                    tagsIds = state.searchFilters.tagsIds.fastFilter { tagId ->
                        event.tags.fastAny { it.id == tagId }
                    },
                ),
            )
        }
    }

    private fun reduceRecentSearchesLoading(event: RecentSearchesLoading) = when (event) {
        is RecentSearchesLoading.Started -> state { copy(recentSearches = recentSearches.toLoading()) }
        is RecentSearchesLoading.Succeed -> state { copy(recentSearches = Lce.Content(event.reviews)) }
        is RecentSearchesLoading.Failed -> state { copy(recentSearches = Lce.Error(event.error)) }
    }

    private fun reduceReviewsLoading(event: ReviewsLoading) = when (event) {
        is ReviewsLoading.Started -> state { copy(allReviews = allReviews.toLoading()) }
        is ReviewsLoading.Succeed -> {
            state { copy(allReviews = Lce.Content(event.reviews)) }
            updateSearchConditions()
        }
        is ReviewsLoading.Failed -> state { copy(allReviews = Lce.Error(event.error)) }
    }

    private fun reduceReviewsFiltered(event: ReviewsFiltered) {
        state { copy(searchedReviews = event.reviews) }
    }

    private fun updateSearchConditions(
        query: String = state.searchQuery,
        filters: SearchFilters = state.searchFilters,
        sorting: ReviewsSortingStrategy = state.sortingStrategy,
    ) {
        state {
            copy(
                searchFilters = filters,
                searchQuery = query,
                sortingStrategy = sorting,
            )
        }

        commands(
            FilterReviews(
                reviews = state.allReviews.content.orEmpty(),
                query = state.searchQuery,
                filters = state.searchFilters,
                sorting = state.sortingStrategy,
            )
        )
    }

}