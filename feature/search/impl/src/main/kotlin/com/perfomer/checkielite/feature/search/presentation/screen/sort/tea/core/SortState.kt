package com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core

import com.perfomer.checkielite.core.data.datasource.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.core.data.datasource.sort.SortingOrder

internal data class SortState(
    val currentOption: ReviewsSortingStrategy,
    val currentOrder: SortingOrder,
    val sortingOptions: List<ReviewsSortingStrategy> = ReviewsSortingStrategy.entries,
)