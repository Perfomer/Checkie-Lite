package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core

import com.perfomer.checkielite.common.pure.state.Lce
import com.perfomer.checkielite.common.pure.state.content
import com.perfomer.checkielite.core.domain.entity.review.CheckieReview
import com.perfomer.checkielite.core.domain.entity.review.CheckieTag
import com.perfomer.checkielite.core.domain.entity.search.SearchFilters
import com.perfomer.checkielite.core.domain.entity.sort.ReviewsSortingStrategy

internal data class SearchState(
    val recentSearches: Lce<List<CheckieReview>> = Lce.initial(),
    val allReviews: Lce<List<CheckieReview>> = Lce.initial(),
    val allTags: Lce<List<CheckieTag>> = Lce.initial(),
    val searchedReviews: List<CheckieReview> = emptyList(),
    val searchQuery: String = "",
    val searchFilters: SearchFilters = SearchFilters(),
    val sortingStrategy: ReviewsSortingStrategy = ReviewsSortingStrategy.RELEVANCE,
) {

    val hasSearchConditions: Boolean = searchQuery.isNotBlank() || !searchFilters.isEmpty || sortingStrategy != ReviewsSortingStrategy.RELEVANCE

    val currentReviews: List<CheckieReview> =
        if (hasSearchConditions) searchedReviews
        else recentSearches.content.orEmpty()
}