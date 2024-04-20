package com.perfomer.checkielite.feature.search.presentation.screen.search.tea.core

import com.perfomer.checkielite.core.data.datasource.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.core.data.datasource.sort.SortingOrder

internal data class SearchState(
    val searchQuery: String = "",
    val currentSortStrategy: ReviewsSortingStrategy = ReviewsSortingStrategy.CREATION_DATE,
    val currentSortOrder: SortingOrder = SortingOrder.DESCENDING,
)