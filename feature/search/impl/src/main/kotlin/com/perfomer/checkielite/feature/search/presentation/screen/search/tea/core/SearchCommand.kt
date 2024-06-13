package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core

import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.search.SearchFilters
import com.perfomer.checkielite.core.entity.search.SearchSorting

internal sealed interface SearchCommand {

    data object LoadReviews : SearchCommand

    data object LoadTags : SearchCommand

    data object LoadRecentSearches : SearchCommand

    data object ClearRecentSearches : SearchCommand

    class FilterReviews(
        val reviews: List<CheckieReview>,
        val query: String,
        val filters: SearchFilters,
        val sorting: SearchSorting,
    ) : SearchCommand

    class RememberRecentSearch(val reviewId: String) : SearchCommand
}

internal sealed interface SearchNavigationCommand : SearchCommand {

    data object Exit : SearchNavigationCommand

    class OpenReviewDetails(val reviewId: String) : SearchNavigationCommand

    class OpenAllFilters(
        val currentFilters: SearchFilters,
        val currentSorting: SearchSorting,
    ) : SearchNavigationCommand

    class OpenSort(val currentSorting: SearchSorting) : SearchNavigationCommand

    class OpenTags(val selectedTagsIds: List<String>) : SearchNavigationCommand
}