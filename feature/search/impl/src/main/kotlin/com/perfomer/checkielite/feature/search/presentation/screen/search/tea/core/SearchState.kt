package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.content
import com.perfomer.checkielite.core.entity.CheckieReview
import com.perfomer.checkielite.core.entity.search.SearchFilters
import com.perfomer.checkielite.core.entity.search.SearchSorting

internal data class SearchState(
    val recentSearches: Lce<List<CheckieReview>> = Lce.initial(),
    val allReviews: Lce<List<CheckieReview>> = Lce.initial(),
    val searchedReviews: List<CheckieReview> = emptyList(),
    val searchQuery: String = "",
    val searchFilters: SearchFilters = SearchFilters(),
    val searchSorting: SearchSorting = SearchSorting(),
) {

    val hasSearchConditions: Boolean = searchQuery.isNotBlank() || !searchFilters.isEmpty || searchSorting != SearchSorting.default

    val currentReviews: List<CheckieReview> =
        if (hasSearchConditions) searchedReviews
        else recentSearches.content.orEmpty()
}