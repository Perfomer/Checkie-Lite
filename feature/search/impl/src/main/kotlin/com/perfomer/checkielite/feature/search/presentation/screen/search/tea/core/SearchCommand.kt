package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core

import com.perfomer.checkielite.core.entity.CheckieTag
import com.perfomer.checkielite.core.entity.search.SearchFilters
import com.perfomer.checkielite.core.entity.search.SearchSorting

internal sealed interface SearchCommand {

    class SearchReviews(
        val query: String,
        val filters: SearchFilters,
        val sorting: SearchSorting,
    ) : SearchCommand

    data object LoadRecentSearches : SearchCommand

    data object ClearRecentSearches : SearchCommand

    class RememberRecentSearch(val reviewId: String) : SearchCommand
}

internal sealed interface SearchNavigationCommand : SearchCommand {

    data object Exit : SearchNavigationCommand

    class OpenReviewDetails(val reviewId: String) : SearchNavigationCommand

    class OpenSort(val currentSorting: SearchSorting) : SearchNavigationCommand

    class OpenTags(val selectedTags: List<CheckieTag>) : SearchNavigationCommand
}