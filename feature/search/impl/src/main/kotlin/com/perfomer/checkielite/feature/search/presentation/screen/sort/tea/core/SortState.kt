package com.perfomer.checkielite.feature.search.presentation.screen.sort.tea.core

import com.perfomer.checkielite.core.entity.sort.ReviewsSortingStrategy
import com.perfomer.checkielite.core.entity.sort.SortingOrder

internal data class SortState(
    val currentOption: ReviewsSortingStrategy,
    val currentOrder: SortingOrder,
    val sortingOptions: List<ReviewsSortingStrategy> = ReviewsSortingStrategy.entries,
)